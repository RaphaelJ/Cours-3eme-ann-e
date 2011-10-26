#ifndef SERVER_H
#define SERVER_H

#include <stdlib.h>
#include <stdio.h>

#include <iostream>
#include <list>
#include <queue>

#include "Utils/Async.h"

#include "Sockets/ClientSocket.h"
#include "StatusServer.h"
#include "TerminalServer.h"
#include "AdminServer.h"
#include "InOutServer.h"

extern int docked_ferries[];

extern queue<int> waiting_ferries;
extern pthread_mutex_t mutex_waiting;

extern list<int> leaving_ferries;
extern pthread_mutex_t mutex_leaving;

extern list<int> connected_clients;
extern pthread_mutex_t mutex_connected;

extern list<ClientSocket> status_clients;
extern pthread_mutex_t mutex_status;

#endif // SERVER_H