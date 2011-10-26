#ifndef TERMINALSERVER_H
#define TERMINALSERVER_H

#include <stdlib.h>
#include <stdio.h>

#include <algorithm>
#include <iostream>
#include <list>
#include <queue>

#include "IniParser/IniParser.h"
#include "Sockets/ClientSocket.h"
#include "Sockets/ServerSocket.h"
#include "Utils/Exception.h"
#include "Utils/Time.h"

#include "TerminalProtocol.h"
#include "Server.h"

void *terminal_server(void* arg);

#endif // TERMINALSERVER_H