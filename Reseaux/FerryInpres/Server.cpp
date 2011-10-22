#include <stdlib.h>
#include <stdio.h>

#include <iostream>

#include "IniParser/IniParser.h"
#include "Sockets/ClientSocket.h"
#include "Sockets/ServerSocket.h"
#include "Utils/Async.h"
#include "Utils/Exception.h"

#include "Protocol.h"

// Vecteur donnant le numero du ferry présent dans chaque terminal
// (0 si aucun ferry)
static int registered_ferries[] = { 3, 0, 17, 2, 3 };

void user_login(ClientSocket sock);
void next_departure(ClientSocket sock, int terminal_id);

int main(int argc, char **argv)
{
    async_call(server_terminals, NULL);
    
    return EXIT_SUCCESS;
}

// Serveur assurant la gestion des terminaux.
void *server_terminals(void* arg)
{
    with_server_socket(39001, user_login);
    return NULL;
}

// Gère la connexion d'un utilisateur distant sur l'un des terminaux.
// Si les informations de connexion ne sont pas valides, termine la connexion.
void user_login(ClientSocket sock)
{
    protocol packet = sock.receive<protocol>();
    
    if (packet.type == protocol::LOGIN) {
        try {
            IniParser agents("agents.ini");
            
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
    
    // Lors d'erreur ou lors de la fin de la session, envoie un packet CLOSE
    send_flag_packet(sock, protocol::CLOSE);
}

// Gère la demande du prochain départ d'un ferry.
// Indique si un ferry sort du terminal ce jour, s'il n'y a pas de ferry à ce
// terminal
void next_departure(ClientSocket sock, int terminal_id)
{
    protocol packet = sock.receive<protocol>();
    
    if (packet.type == protocol::NEXT_DEPARTURE) {
        if (registered_ferries[terminal_id]) { // Si un ferry est au terminal   
            try { // Départ connu
                IniParser departures("departures.ini");
                char terminal_id_str[16];

                // Copie l'heure dans le packet
                sprintf(terminal_id_str, "%d", terminal_id);
                strcpy(
                    packet.content.departure_known,
                    departures.get_value(string(terminal_id_str)).c_str()
                );
                packet.type = protocol::DEPARTURE_KNOWN;
            } catch (Exception e) { // Pas de départ trouvé
                packet.type = protocol::DEPARTURE_UNKNOWN;
            }
        } else { // Pas de ferry sur ce terminal
            packet.type = protocol::NO_FERRY;
        }
        sock.send<protocol>(packet);
    }
}
