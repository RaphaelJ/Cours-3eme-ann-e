#include <iostream>

#include "IniParser/IniParser.h"
#include "Sockets/ClientSocket.h"
#include "Utils/Time.h"

#include "TerminalProtocol.h"

void _user_login(ClientSocket sock);
void _next_departure(ClientSocket sock);
void _manage_begin_loading(ClientSocket sock);
void _manage_end_loading(ClientSocket sock);
void _manage_leaving(ClientSocket sock);
void _manage_ask_for_ferry(ClientSocket sock);
void _manage_docking(ClientSocket sock, int ferry_id);

// Se connecte au serveur des terminaux (l'ip du serveur doit être renseignée
// dans le ligne de commande).
int main(int argc, char **argv)
{
    IniParser properties("terminal_client.ini");
    int server_port = atoi(properties.get_value("server_port").c_str());
    const char *ip = properties.get_value("server_ip").c_str();
    
    ClientSocket sock(ip, server_port);
        
    _user_login(sock);

//     // Lors d'erreur ou lors de la fin de la session, envoie un packet CLOSE
//     protocol packet; 
//     packet.type = protocol::CLOSE;
//     packet.content.close = current_time();
//     sock.send<protocol>(&packet);
    
    sock.close();
    
    printf("Fin de la connexion\n");
}

// Identifie l'utilisateur au serveur des terminaux
void _user_login(ClientSocket sock)
{
    terminal_protocol packet;
    
    // Envoi des informations de connexion
    packet.type = terminal_protocol::LOGIN;
    printf("Nom d'utilisateur: ");
    scanf("%s", packet.content.login.user);
    printf("Mot de passe: ");
    scanf("%s", packet.content.login.password);
    printf("Numero du terminal: ");
    scanf("%d", &packet.content.login.terminal_id);
    getchar();
    sock.send<terminal_protocol>(&packet);
    
    sock.receive<terminal_protocol>(&packet);
    if (packet.type == terminal_protocol::FAIL) {
        printf("Mauvais identifiants de connexion ou terminal occupé\n");
    } else if (packet.type == terminal_protocol::ACK) {
        printf("Identification reussie\n");
        return _next_departure(sock);
    }
}

// Demande l'heure du prochain départ
void _next_departure(ClientSocket sock)
{
    printf("Pressez une touche pour demander le prochain depart\n");
    getchar();
    
    send_flag_packet(sock, terminal_protocol::NEXT_DEPARTURE);
    
    terminal_protocol packet;
    sock.receive<terminal_protocol>(&packet);
    switch (packet.type) {
    case terminal_protocol::DEPARTURE_KNOWN:
        printf(
            "Depart prevu a %hd:%hd\n", packet.content.departure_known.hour,
            packet.content.departure_known.min
        );
        return _manage_begin_loading(sock);
        break;
    case terminal_protocol::DEPARTURE_UNKNOWN:
        printf("L'heure de depart n'est pas connue\n");
        return _next_departure(sock);
        break;
    case terminal_protocol::NO_FERRY:
        printf("Il n'y a pas de ferry sur ce terminal\n");
        return _manage_ask_for_ferry(sock);
        break;
    default:
        break;
    }
}

// Gère la demande de commencement de l'embarquement
void _manage_begin_loading(ClientSocket sock)
{
    printf("Pressez une touche pour demander le debut de l'embarquement\n");
    getchar();
    
    terminal_protocol packet;
    
    packet.type = terminal_protocol::BEGIN_LOADING;
    packet.content.begin_loading = current_time();
    sock.send<terminal_protocol>(&packet);
    
    sock.receive<terminal_protocol>(&packet);
    if (packet.type == terminal_protocol::ACK) { 
        printf("L'embarquement peut commencer\n");
        return _manage_end_loading(sock);
    } else if (packet.type == terminal_protocol::FAIL) {
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
    getchar();
    
    terminal_protocol packet;
    
    packet.type = terminal_protocol::END_LOADING;
    packet.content.end_loading = current_time();
    sock.send<terminal_protocol>(&packet);
    
    sock.receive<terminal_protocol>(&packet);
    if (packet.type == terminal_protocol::ACK) { 
        printf("Le ferry peut a present sortir du terminal\n");
        return _manage_leaving(sock);
    } else if (packet.type == terminal_protocol::FAIL) {
        printf("L'embarquement doit durer au moins 15 minutes\n");
        return _manage_end_loading(sock);
    }
}

// Gère la notification de départ
void _manage_leaving(ClientSocket sock)
{
    printf("Pressez une touche pour signaler le depart du ferry\n");
    getchar();
    
    terminal_protocol packet;
    
    packet.type = terminal_protocol::FERRY_LEAVING;
    packet.content.ferry_leaving = current_time();
    sock.send<terminal_protocol>(&packet);
    
    return _manage_ask_for_ferry(sock);
}

// Gère la demande d'accostage d'un nouveau ferry
void _manage_ask_for_ferry(ClientSocket sock)
{
    printf("Pressez une touche pour faire une demande d'accostage\n");
    getchar();
    
    send_flag_packet(sock, terminal_protocol::ASK_FOR_FERRY);
    
    terminal_protocol packet;
    sock.receive<terminal_protocol>(&packet);
    
    if (packet.type == terminal_protocol::FERRY_RESERVED) { // Un ferry va se présenter
        printf(
            "Le ferry numero %d va se presenter\n",
            packet.content.ferry_reserved
        );
        
        return _manage_docking(sock, packet.content.ferry_reserved);
    } else if (packet.type == terminal_protocol::FAIL) {
        // Pas de ferry en attente d'accostage
        printf("Il n'y a pas de ferry en attente d'accostage\n");
        return _manage_ask_for_ferry(sock);
    }
}

// Gère la notification d'accostage d'un nouveau ferry
void _manage_docking(ClientSocket sock, int ferry_id)
{
    printf(
        "Pressez une touche si le ferry numero %d s'est presente au terminal\n",
        ferry_id
    );
    getchar();
    
    terminal_protocol packet;
    packet.type = terminal_protocol::FERRY_ARRIVING;
    packet.content.ferry_arriving.ferry_id = ferry_id;
    packet.content.ferry_arriving.time = current_time();
    
    sock.send<terminal_protocol>(&packet);
    
    sock.receive<terminal_protocol>(&packet); // Reception de l'ACK
    
    return _next_departure(sock);
}