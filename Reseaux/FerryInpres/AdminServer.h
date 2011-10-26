#ifndef ADMIN_SERVER_H
#define ADMIN_SERVER_H

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

void *admin_server(void* arg);

#endif // ADMIN_SERVER_H