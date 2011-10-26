#ifndef ADMINPROTOCOL_H
#define ADMINPROTOCOL_H

#define STR_LENGTH 30
#define TIME_LENGTH 6
#define INT_LENGTH 12

// Requête transmise/reçue via le réseau.
typedef struct _admin_protocol {
    enum protocol_type : char {
        ACK = 'A', FAIL = 'F', LOGIN = 'L', LCLIENTS = 'C', PAUSE = 'P',
        STOP = 'S'
    } type;
    
    union protocol_content {
        // Demande la connexion avec un utilisateur et un mot de passe
        struct login_protocol {
            char user[STR_LENGTH];
            char password[STR_LENGTH];
        } login;
        
        // Donne tous les clients connectés
        struct list_clients_protocol {
            char n_clients[INT_LENGTH];
            char client[INT_LENGTH];
        } list_clients;
        
        // Donne le nombre de secondes avant l'arret du serveur
        char stop[INT_LENGTH];
    } content;
} admin_protocol;

#endif // ADMINPROTOCOL_H