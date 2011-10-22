#include <stdlib.h>
#include <stdio.h>

#include <iostream>
#include <list>
#include <queue>

#include "IniParser/IniParser.h"
#include "Sockets/ClientSocket.h"
#include "Sockets/ServerSocket.h"
#include "Utils/Async.h"
#include "Utils/Exception.h"
#include "Utils/Time.h"

#include "Protocol.h"

typedef struct _ferry {
    int id;
    enum FERRY_STATE {
        NO_FERRY, ARRIVING, DOCKED, LOADING, LOADED
    } state;
    s_time last_change;
} ferry;

// Vecteur donnant le numero du ferry présent dans chaque terminal
// (id = 0 si aucun ferry)
static ferry docked_ferries[] = { 
    { 3, ferry::DOCKED }, { 0, ferry::NO_FERRY }, { 17, ferry::DOCKED },
    { 2, ferry::DOCKED }, { 3, ferry::DOCKED }
};

// Ferrys en attente de traitement dans l'un des terminaux
static queue<int> waiting_ferries;
pthread_mutex_t mutex_waiting;

// Ferrys en cours de départ
static list<int> leaving_ferries;
pthread_mutex_t mutex_leaving;

void user_login(ClientSocket sock);
void next_departure(ClientSocket sock, int terminal_id);

int main(int argc, char **argv)
{
    pthread_mutex_init(&mutex_waiting);
    pthread_mutex_init(&mutex_leaving);
    
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
                return next_departure(sock, packet.content.login.terminal_id);
            } else // Mauvais mot de passe
                send_flag_packet(sock, protocol::FAIL);
        } catch (Exception e) { // Utilisateur inexistant
            send_flag_packet(sock, protocol::FAIL);
        }
    }
    
    // Lors d'erreur ou lors de la fin de la session, envoie un packet CLOSE
    packet.type = protocol::CLOSE;
    packet.content.close = current_time();
}

// Donne l'heure de départ du ferry depuis le fichier derpartures.ini
s_time _ferry_departure(int ferry_id)
{
    IniParser departures("departures.ini");
    char ferry_id_str[16];
                
    sprintf(ferry_id_str, "%d", ferry_id);
    
    return str_to_time(departures.get_value(string(ferry_id_str)).c_str());
}

// Gère la demande du prochain départ d'un ferry.
// Indique si un ferry sort du terminal ce jour, s'il n'y a pas de ferry à ce
// terminal
void next_departure(ClientSocket sock, int terminal_id)
{
    protocol packet = sock.receive<protocol>();
    
    if (packet.type == protocol::NEXT_DEPARTURE) {
        if (docked_ferries[terminal_id].state != ferry::NO_FERRY) {
            try { // Départ connu
                packet.type = protocol::DEPARTURE_KNOWN;
                packet.content.departure_known = _ferry_departure(
                    docked_ferries[terminal_id].id
                );
                sock.send<protocol>(packet);
                
                return manage_leaving(sock, terminal_id); // Gère l'appareillage
            } catch (Exception e) { // Pas de départ trouvé
                send_flag_packet(sock, protocol::DEPARTURE_UNKNOWN);
                return next_departure(sock, terminal_id); // Se remet en attente
            }
        } else { // Pas de ferry sur ce terminal
            send_flag_packet(sock, protocol::NO_FERRY);
            return manage_docking(sock, terminal_id); // Gère l'accostage
        }
    }
}

// Gère les opérations qu'un ferry peut effectuer lorsqu'il est présent à 
// un terminal pour la préparation de son appareillage.
void manage_leaving(ClientSocket sock, int terminal_id)
{
    protocol packet = sock.receive<protocol>();
    
    if (docked_ferries[terminal_id].state == ferry::DOCKED
        && packet.type == protocol::BEGIN_LOADING) {
        // Demande le début du chargement
        s_time departure = _ferry_departure(
            docked_ferries[terminal_id].id
        );
        s_time loading = packet.content.begin_loading;
        
        if (time_span(loading, departure) <= 45) {
            // Le chargement ne peut commencer que 45 minutes avant le départ
            docked_ferries[terminal_id].state = ferry::LOADING;
            docked_ferries[terminal_id].last_change = loading;
            send_flag_packet(sock, protocol::ACK);
        } else {
            send_flag_packet(sock, protocol::FAIL);
        }
        
        return manage_leaving(sock, terminal_id);
    } else if (docked_ferries[terminal_id].state == ferry::LOADING
               && packet.type == protocol::END_LOADING) {
        // Confirme la fin du chargement
        s_time start_loading = docked_ferries[terminal_id].last_change;
        s_time end_loading = packet.content.end_loading;
        
        if (time_span(start_loading, end_loading) >= 15) {
            // Le chargement dure au minimum 15 minutes
            docked_ferries[terminal_id].state = ferry::LOADED;
            docked_ferries[terminal_id].last_change = end_loading;
            send_flag_packet(sock, protocol::ACK);
        } else {
            send_flag_packet(sock, protocol::FAIL);
        }
        
        return manage_leaving(sock, terminal_id);
    } else if (docked_ferries[terminal_id].state == ferry::LOADED
               && packet.type == protocol::FERRY_LEAVING) {
        // Ajoute le ferry aux ferry en cours de départ
        pthread_mutex_lock(&mutex_leaving);
        leaving_ferries.push_front(docked_ferries[terminal_id].id);
        pthread_mutex_unlock(&mutex_leaving);
        
        // Supprime le ferry du terminal
        docked_ferries[terminal_id].id = 0;
        docked_ferries[terminal_id].state = 0;
        
        return manage_docking(sock, terminal_id); // Terminal vide.
    }
}

// Gère les opérations de dockage d'un ferry lorsque le terminal est vide
void manage_docking(ClientSocket sock, int terminal_id)
{
    protocol packet = sock.receive<protocol>();
    
    if (docked_ferries[terminal_id].state == ferry::NO_FERRY
        && packet.type == protocol::ASK_FOR_FERRY) {
        // Attribue un nouveau ferry au terminal
        pthread_mutex_lock(&mutex_waiting);
        if (waiting_ferries.empty()) { // Pas de ferry en attente
            pthread_mutex_unlock(&mutex_waiting);
            
            send_flag_packet(sock, protocol::FAIL);
        } else { // Un ferry en attente
            int ferry_id = waiting_ferries.front();
            waiting_ferries.pop();
            pthread_mutex_unlock(&mutex_waiting);
    
            docked_ferries[terminal_id].id = ferry_id;
            docked_ferries[terminal_id].state = ferry::ARRIVING;
            
            send_flag_packet(sock, protocol::ACK);
        }
    
        return manage_docking(sock, terminal_id);
    } else if (docked_ferries[terminal_id].state == ferry::ARRIVING
               && packet.type == protocol::FERRY_ARRIVING) {
        // Change l'état du ferry comme arrivé au terminal
        docked_ferries[terminal_id].state = ferry::DOCKED;
        
        return next_departure(sock, terminal_id);
    }
}