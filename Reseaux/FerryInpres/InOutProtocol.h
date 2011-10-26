#ifndef INOUT_PROTOCOL_H
#define INOUT_PROTOCOL_H

#include <stdio.h>
#include <time.h>

#include "Sockets/ClientSocket.h"
#include "Utils/Time.h"

#define TIME_LENGTH 6
#define INT_LENGTH 12

// Requête transmise/reçue via le réseau.
typedef struct _inout_protocol {
    enum protocol_type : char {
        SHIP_IN = 'I',
        SHIP_OUT = 'O'
    } type;
    
    char ferry_id[INT_LENGTH];
    char time[TIME_LENGTH];
} inout_protocol;

#endif // INOUT_PROTOCOL_H