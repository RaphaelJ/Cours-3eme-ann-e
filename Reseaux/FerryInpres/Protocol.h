#ifndef PROTOCOL_H
#define PROTOCOL_H

#include <time.h>

#include "Sockets/ClientSocket.h"

#define STR_LENGTH 30

// Stocke l'heure
typedef struct _s_time {
    short hour;
    short min;
} s_time;

// Requête transmise/reçue via le réseau.
typedef struct _protocol {
    // Type de requête
    enum protocol_type {
        ACK, FAIL, LOGIN, NEXT_DEPARTURE, DEPARTURE_KNOWN, DEPARTURE_UNKNOWN,
        NO_FERRY, BEGIN_LOADING, END_LOADING, FERRY_LEAVING, ASK_FOR_FERRY,
        FERRY_ARRIVING, CLOSE
    } type;
    
    // Contenu de la requête
    union protocol_content {
        // Demande la connexion avec un utilisateur et un mot de passe
        struct login_protocol {
            char user[STR_LENGTH];
            char password[STR_LENGTH];
            int terminal_id;
        } login;
        
        // Donne l'heure du prochain départ
        char departure_known[5];
        
        // Demande l'autorisation du début de l'embarquement
        char begin_loading[5];
        
        // Notifie la fin de l'embarquement et demande l'autorisation de partir
        char end_loading[5];
        
        // Notifie que le ferry quitte le terminal
        char leaving[5];
        
        // Notifie l'arrivée d'un ferry
        struct ferry_arriving_protocol {
            char time;
            char ferry_name[STR_LENGTH];
        } ferry_arriving;
    } content;
} protocol;

// Envoie un packet qui ne contient aucune information si ce n'est son type.
inline void send_flag_packet(ClientSocket sock, protocol::protocol_type type)
{
    protocol packet;
    packet.type = type;
    sock.send<protocol>(packet);
}

#endif // PROTOCOL_H