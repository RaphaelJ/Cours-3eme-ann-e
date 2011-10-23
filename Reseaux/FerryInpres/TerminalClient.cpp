#include <iostream>

#include "IniParser/IniParser.h"
#include "Sockets/ClientSocket.h"
#include "Utils/Time.h"

#include "Protocol.h"

// Se connecte au serveur des terminaux (l'ip du serveur doit être renseignée
// dans le ligne de commande).
int main(int argc, char **argv)
{
    IniParser properties("terminal_server.ini");
    int server_port = atoi(properties.get_value("port").c_str());
    ClientSocket sock(argv[1], server_port);
    
    _user_login(sock);

    // Lors d'erreur ou lors de la fin de la session, envoie un packet CLOSE
    protocol packet; 
    packet.type = protocol::CLOSE;
    packet.content.close = current_time();
    sock.send<protocol>(packet);
    
    sock.close();
}

// Identifie l'utilisateur au serveur des terminaux
void _user_login(ClientSocket sock)
{
    protocol packet;
    
    // Envoi des informations de connexion
    packet.type == protocol::LOGIN;
    printf("Nom d'utilisateur: ");
    scanf("%s", packet.content.login.user);
    printf("Mot de passe: ");
    scanf("%s", packet.content.login.password);
    printf("Numero du terminal: ");
    scanf("%d", packet.content.login.terminal_id);
    
    sock.send<protocol>(packet);
    
    sock.receive<protocol>(&packet);
    if (packet.type == protocol::FAIL) {
        printf("Mauvais identifiants de connexion");
    } else if (packet.type == protocol::ACK) {
        printf("Identification reussie");
        return _next_departure(sock);
    }
}

// Demande l'heure du prochain départ
void _next_departure(ClientSocket sock)
{
    printf("Pressez une touche pour demander le prochain depart\n");
    pause();
    
    send_flag_packet(sock, protocol::NEXT_DEPARTURE);
    
    protocol packet;
    sock.receive<protocol>(&packet);
    switch (packet.type) {
    case protocol::DEPARTURE_KNOWN:
        printf(
            "Depart prevu a %hd:%hd\n", packet.content.departure_known.hour,
            packet.content.departure_known.min
        );
        return _manage_begin_loading(sock);
        break;
    case protocol::DEPARTURE_UNKNOWN:
        printf("L'heure de depart n'est pas connue\n");
        return _next_departure(sock);
        break;
    case protocol::NO_FERRY:
        printf("Il n'y a pas de ferry sur ce terminal\n");
        return _manage_ask_for_ferry(sock);
        break;
    }
}

// Gère la demande de commencement de l'embarquement
void _manage_begin_loading(ClientSocket sock)
{
    printf("Pressez une touche pour demander le debut de l'embarquement'\n");
    pause();
    
    protocol packet;
    
    packet.type = protocol::BEGIN_LOADING;
    packet.content.begin_loading = current_time();
    sock.send<protocol>(packet);
    
    sock.receive<protocol>(&packet);
    if (packet.type == protocol::ACK) { 
        printf("L'embarquement peut commencer\n");
        return _manage_end_loading(sock);
    } else if (packet.type == protocol::FAIL) {
        printf(
            "L'embarquement ne peut se produire que 45 minutes avant le "
            "depart\n"
        );
        return _manage_begin_loading(sock);
    }    
}

// Gère la notification de fin de l'embarquement
void _manage_end_loading(ClientSocket sock)
{
    printf("Pressez une touche pour signaler la fin de l'embarquement\n");
    pause();
    
    protocol packet;
    
    packet.type = protocol::END_LOADING;
    packet.content.end_loading = current_time();
    sock.send<protocol>(packet);
    
    sock.receive<protocol>(&packet);
    if (packet.type == protocol::ACK) { 
        printf("Le ferry peut a present sortir du terminal\n");
        return _manage_leaving(sock);
    } else if (packet.type == protocol::FAIL) {
        printf("L'embarquement doit durer au moins 15 minutes\n");
        return _manage_end_loading(sock);
    }
}

// Gère la notification de départ
void _manage_leaving(ClientSocket sock)
{
    printf("Pressez une touche pour signaler le depart du ferry\n");
    pause();
    
    protocol packet;
    
    packet.type = protocol::FERRY_LEAVING;
    packet.content.ferry_leaving = current_time();
    sock.send<protocol>(packet);
    
    return _manage_ask_for_ferry(sock);
}

// Gère la notification de départ
void _manage_leaving(ClientSocket sock)
{
    
}