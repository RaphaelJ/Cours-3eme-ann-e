#include <iostream>

#include <stdio.h>
#include <string.h>

#include "Sockets/ClientSocket.h"
#include "Utils/Async.h"

#define PORT_SERVEUR 39020
#define HOTE_SERVEUR "127.0.0.1"
#define CHAT_GROUP "224.0.0.1"

typedef enum _protocol_type : char {
    ACK = 'A',
    FAIL = 'F',
    QUESTION = 'Q',
    ANSWER = 'A',
    EVENT = 'E'
} protocol_type;

int identificationAgent(char *user);
void chat(char *user, int port_chat);
void* receptionMessages(void *v_sock);

// Se connecte au serveur de chat pour demander l'authorisation.
int main(int argc, char **argv)
{
    char user[45];
    int port_chat = identificationAgent(user);
    
    chat(user, port_chat);
    
    return 0;
}

int identificationAgent(char *user)
{
    int port_chat;
    
    do { 
        char pass[45];
        ClientSocket sock(HOTE_SERVEUR, PORT_SERVEUR);
        
        printf("Nom agent: \n");
        gets(user);
        printf("Mot de passe: \n");
        gets(pass);
        
        sock.send_string(user);
        sock.send_string(pass);
        
        char c;
        sock.receive<char>(&c);
        if (c == ACK) {
            printf("Identification réussie\n");
            
            char port_str[12];
            sock.receive_string(port_str);
            port_chat = atoi(port_str);
            
            printf("Port du chat: %d\n", port_chat);
        } else {
            printf("Mauvais identifiants\n");
            port_chat = -1;
        }
    } while (port_chat == -1);
    
    return port_chat;
}

typedef struct _arg_reception {
    int socket;
    int port_chat;
} arg_reception;

void chat(char *user, int port_chat)
{
    int sock = socket_utils::socket(AF_INET, SOCK_DGRAM, 0);
    
    struct sockaddr_in dest = {0};
    dest.sin_family = AF_INET;
    dest.sin_addr.s_addr = inet_addr(CHAT_GROUP);
    dest.sin_port = htons(port_chat);
    
    unsigned char ttl = 1;
    setsockopt(sock, IPPROTO_IP, IP_MULTICAST_TTL, (char *) &ttl, sizeof(ttl));

    char help[] = 
            "Précédez votre message par Q pour une question, par A suivi de "
            "l'identifiant d'une question pour une réponse et E pour un "
            "événement.";
            
    printf("%s\n", help);
      
    socket_utils::bind(
        sock, (struct sockaddr*) &dest, sizeof (struct sockaddr_in)
    );
    
    // S'inscrit au groupe
    struct ip_mreq mreq;
    memcpy(&mreq.imr_multiaddr, &dest.sin_addr, sizeof (struct sockaddr_in));
    mreq.imr_interface.s_addr = htonl(INADDR_ANY);
    setsockopt (sock, IPPROTO_IP, IP_ADD_MEMBERSHIP, &mreq, sizeof mreq);
    
    arg_reception *arg = new arg_reception;
    arg->port_chat = port_chat;
    arg->socket = sock; 
    
    async_call(receptionMessages, arg);
    
    for (;;) {
            char message[1000];
            char buffer[1000] = { 0 };
            printf("> ");
            
            gets(message);
            
            if (strlen(message) == 0)
                continue;
            
            switch (message[0]) {
            case 'Q':
                buffer[0] = QUESTION;
                strcpy(buffer + 1, user);
                strcpy(buffer + strlen(user) + 2, message + 2);
                break;
            case 'A':
                buffer[0] = ANSWER;
                strcpy(buffer + 1, user);
                
                // Extrait le numero de question
                char *c;
                for (c = message + 2; *c != ' '; c++);
                *c = '\0';
                strcpy(
                    buffer + strlen(user) +  2, message + 2
                );
                
                strcpy(
                    buffer + strlen(user) + strlen(message + 2) + 3, c + 1
                );
                break;
            case 'E':
                buffer[0] = EVENT;
                strcpy(buffer + 1, user);
                strcpy(buffer + strlen(user) + 2, message + 2);
                break;
            default:
                printf("Message non valide.\n%s\n", help);
                continue;
            }
            
            sendto(
                sock, buffer, sizeof buffer, 0, (struct sockaddr*) &dest, 
                sizeof dest
            );
        }
}

void* receptionMessages(void *v_arg)
{
    arg_reception *arg = (arg_reception *) v_arg;
    int port_chat = arg->port_chat;
    int sock = arg->socket;
    
    for (;;) {
        char *user;
        char buf[1000];
        struct sockaddr_in client = {0};
        client.sin_addr.s_addr = htonl(INADDR_ANY);
        client.sin_port = htons(port_chat);
        client.sin_family = AF_INET;
        
        socklen_t clientLen = sizeof client;
        
        recvfrom(
            sock, buf, 1000, 0, (struct sockaddr*) &client, &clientLen
        );
        
        user = buf + 1;

        printf("\n");
        
        char *question, *idQuestion, *reponse, *evenement, *c;
        int hash = 0;
        switch (buf[0]) {
        case 'Q':
            question = user + strlen(user) + 2;
            
            for (c = question; *c != '\0'; c++)
                hash += *c;
            
            printf("<%s> Question #%d: %s\n", user, hash, question);
            break;
        case 'A':
            idQuestion = user + strlen(user) + 1;
            reponse = idQuestion + strlen(idQuestion) + 1;
            printf(
                "<%s> Reponse à la question #%s: %s\n", user, idQuestion, reponse
            );
            break;
        case 'E':
            evenement = user + strlen(user) + 1;
            printf("<%s> Evenement: %s\n", user, evenement);
            break;
        default:
            continue;
            break;
        }
    }
    
    delete arg;
    
    return NULL;
}