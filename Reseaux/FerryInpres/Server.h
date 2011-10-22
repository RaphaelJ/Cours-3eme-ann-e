#ifndef SERVER_H
#define SERVER_H

#include <stdlib.h>
#include <stdio.h>

#include <iostream>
#include <list>
#include <queue>

#include "Utils/Async.h"

#include "Protocol.h"
#include "TerminalServer.h"

typedef struct _ferry {
    int id;
    enum FERRY_STATE {
        NO_FERRY, ARRIVING, DOCKED, LOADING, LOADED
    } state;
    s_time last_change;
} ferry;

// Vecteur donnant le numero du ferry présent dans chaque terminal
// (id = 0 si aucun ferry)
extern ferry docked_ferries[];

// Ferrys en attente de traitement dans l'un des terminaux
extern queue<int> waiting_ferries;
extern pthread_mutex_t mutex_waiting;

// Ferrys en cours de départ
extern list<int> leaving_ferries;
extern pthread_mutex_t mutex_leaving;

#endif // SERVER_H