#ifndef SERVERSOCKET_H
#define SERVERSOCKET_H

#include <pthread.h>

#include "SocketsUtils.h"
#include "ClientSocket.h"

using namespace std;

void withServerSocket(const int port, void (*action)(ClientSocket sock));

#endif // SERVERSOCKET_H
