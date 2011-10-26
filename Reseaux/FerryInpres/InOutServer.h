#ifndef INOUT_SERVER_H
#define INOUT_SERVER_H

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

#include "InOutProtocol.h"
#include "Server.h"

void *inout_server(void* arg);

#endif // INOUT_SERVER_H