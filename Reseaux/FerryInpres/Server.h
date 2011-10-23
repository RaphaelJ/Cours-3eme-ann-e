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

extern int docked_ferries[];

extern queue<int> waiting_ferries;
extern pthread_mutex_t mutex_waiting;

extern list<int> leaving_ferries;
extern pthread_mutex_t mutex_leaving;

#endif // SERVER_H