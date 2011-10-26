#ifndef ADMINSERVER_H
#define ADMINSERVER_H

#include <stdlib.h>
#include <stdio.h>

#include <iostream>
#include <list>
#include <queue>

#include "IniParser/IniParser.h"
#include "Sockets/ClientSocket.h"
#include "Sockets/ServerSocket.h"
#include "Utils/Exception.h"
#include "Utils/Time.h"

#include "AdminProtocol.h"
#include "Server.h"
#include "StatusServer.h"

void *admin_server(void* arg);

#endif // ADMINSERVER_H