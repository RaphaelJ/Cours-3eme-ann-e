#include "TerminalServer.h"

void _user_login(ClientSocket sock);
s_time _ferry_departure(int ferry_id);
void _next_departure(ClientSocket sock, int terminal_id);
void _manage_leaving(ClientSocket sock, int terminal_id);
void _manage_docking(ClientSocket sock, int terminal_id);

// Serveur assurant la gestion des terminaux.
void *terminal_server(void* arg)
{
    with_server_socket(39001, _user_login);
    return NULL;
}

// Gère la connexion d'un utilisateur distant sur l'un des terminaux.
// Si les informations de connexion ne sont pas valides, termine la connexion.
void _user_login(ClientSocket sock)
{
    protocol packet = sock.receive<protocol>();
    
    if (packet.type == protocol::LOGIN) {
        try {
            IniParser agents("agents.ini");
            
            const char* pass = agents.get_value(
                string(packet.content.login.user)
            ).c_str();
            
            if (strcmp(pass, packet.content.login.password) == 0) {
                // Connexion réussie
                send_flag_packet(sock, protocol::ACK);
                return _next_departure(sock, packet.content.login.terminal_id);
            } else // Mauvais mot de passe
                send_flag_packet(sock, protocol::FAIL);
        } catch (Exception e) { // Utilisateur inexistant
            send_flag_packet(sock, protocol::FAIL);
        }
    }
    
    // Lors d'erreur ou lors de la fin de la session, envoie un packet CLOSE
    packet.type = protocol::CLOSE;
    packet.content.close = current_time();
    sock.send<protocol>(packet);
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
void _next_departure(ClientSocket sock, int terminal_id)
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
                
                return _manage_leaving(sock, terminal_id); // Gère l'appareillage
            } catch (Exception e) { // Pas de départ trouvé
                send_flag_packet(sock, protocol::DEPARTURE_UNKNOWN);
                return _next_departure(sock, terminal_id); // Se remet en attente
            }
        } else { // Pas de ferry sur ce terminal
            send_flag_packet(sock, protocol::NO_FERRY);
            return _manage_docking(sock, terminal_id); // Gère l'accostage
        }
    }
}

// Gère les opérations qu'un ferry peut effectuer lorsqu'il est présent à 
// un terminal pour la préparation de son appareillage.
void _manage_leaving(ClientSocket sock, int terminal_id)
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
        
        return _manage_leaving(sock, terminal_id);
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
        
        return _manage_leaving(sock, terminal_id);
    } else if (docked_ferries[terminal_id].state == ferry::LOADED
               && packet.type == protocol::FERRY_LEAVING) {
        // Ajoute le ferry aux ferry en cours de départ
        pthread_mutex_lock(&mutex_leaving);
        leaving_ferries.push_front(docked_ferries[terminal_id].id);
        pthread_mutex_unlock(&mutex_leaving);
        
        // Supprime le ferry du terminal
        docked_ferries[terminal_id].id = 0;
        docked_ferries[terminal_id].state = ferry::NO_FERRY;
        
        return _manage_docking(sock, terminal_id); // Terminal vide.
    }
}

// Gère les opérations de dockage d'un ferry lorsque le terminal est vide
void _manage_docking(ClientSocket sock, int terminal_id)
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
            
            packet.type = protocol::FERRY_RESERVED;
            packet.content.ferry_reserved = ferry_id;
            sock.send<protocol>(packet);
        }
        
        return _manage_docking(sock, terminal_id);
    } else if (docked_ferries[terminal_id].state == ferry::ARRIVING
               && packet.type == protocol::FERRY_ARRIVING) {
        int remote_ferry_id = packet.content.ferry_arriving.ferry_id;
        if (docked_ferries[terminal_id].id == remote_ferry_id) {
            // Le ferry en attente est bien arrivé au terminale
            docked_ferries[terminal_id].state = ferry::DOCKED;
            
            send_flag_packet(sock, protocol::ACK);
            
            return _next_departure(sock, terminal_id);
        } else {
            // Le ferry n'est pas celui qui a réservé le terminal
            send_flag_packet(sock, protocol::FAIL);
            
            return _manage_docking(sock, terminal_id);
        }
    }
}