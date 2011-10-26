#ifndef INOUTSERVER_H
#define INOUTSERVER_H

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

#endif // INOUTSERVER_H