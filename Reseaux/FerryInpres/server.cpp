#include <stdlib.h>
#include <stdio.h>

#include <iostream>

#include "Utils/Exception.h"
#include "Sockets/ServerSocket.h"
#include "Sockets/ClientSocket.h"

#include "IniParser/IniParser.h"
#include "Protocol.h"

// Vecteur donnant le numero du ferry présent dans chaque terminal
// (0 si aucun ferry)
static int registered_ferries[] = { 3, 0, 17, 2, 3 };

void user_login(ClientSocket sock);
void next_departure(ClientSocket sock, int terminal_id);

int main(int argc, char **argv)
{
    with_server_socket(39001, user_login);
}

// Gère la connexion d'un utilisateur distant
void user_login(ClientSocket sock)
{
    IniParser agents("agents.ini");
    protocol packet = sock.receive<protocol>();
    
    if (packet.type == protocol::LOGIN) {
        try {
            char* pass = agents.get_value(string(packet.content.login.user));
            
            if (strcmp(pass, packet.content.login.password) == 0) {
                // Connexion réussie
                send_flag_packet(sock, protocol::ACK);
                next_departure(sock, packet.content.login.terminal_id);
                
            } else // Mauvais mot de passe
                send_flag_packet(sock, protocol::FAIL);
        } catch (Exception e) { // Utilisateur inexistant
            send_flag_packet(sock, protocol::FAIL);
        }
    }
}

// Gère la demande du prochain départ d'un ferry
void next_departure(ClientSocket sock, int terminal_id)
{
    protocol packet = sock.receive<protocol>();
    
    if (packet.type == protocol::NEXT_DEPARTURE) {
        
        sock.send<protocol>();
    }
}