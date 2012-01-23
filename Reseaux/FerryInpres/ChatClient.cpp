#include <iostream>

#include <stdio.h>
#include <string.h>

#include "Sockets/ClientSocket.h"
#include "Utils/Async.h"

#define PORT_SERVEUR 39020
#define HOTE_SERVEUR "127.0.0.1"

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

void chat(char *user, int port_chat)
{
    int sock = socket_utils::socket(AF_INET, SOCK_DGRAM, 0);

    struct sockaddr_in dest = {0};
    dest.sin_addr.s_addr = htonl(INADDR_ANY);;
    dest.sin_port = htons(port_chat);
    dest.sin_family = AF_INET;

    char help[] = 
            "Précédez votre message par Q pour une question, par A suivi de "
            "l'identifiant d'une question pour une réponse et E pour un "
            "événement.";
            
    printf("%s\n", help);
      
    socket_utils::bind(
        sock, (struct sockaddr*) &dest, sizeof (struct sockaddr_in)
    );
    
    async_call(receptionMessages, (void *) &sock);
    
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

void* receptionMessages(void *v_sock)
{
    int sock = *((int *) v_sock);
    
    for (;;) {
        char *user;
        char buf[1000];
        struct sockaddr_in client = {0};
        
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
    
    return NULL;
}