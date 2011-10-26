#ifndef STATUSPROTOCOL_H
#define STATUSPROTOCOL_H

// Requête transmise/reçue via le réseau.
typedef struct _status_protocol {
    enum protocol_type : char {
        PAUSE = 'P', STOP = 'S'
    } type;
} status_protocol;

#endif // STATUSROTOCOL_H
