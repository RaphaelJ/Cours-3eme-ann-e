#ifndef STATUSSERVER_H
#define STATUSSERVER_H

#include <stdlib.h>
#include <stdio.h>

#include <list>

#include "IniParser/IniParser.h"
#include "Sockets/ClientSocket.h"
#include "Sockets/ServerSocket.h"
#include "Sockets/SocketException.h"

#include "Server.h"
#include "StatusProtocol.h"

void *status_server(void* arg);
void signal_status(char status);

#endif // STATUSSERVER_H