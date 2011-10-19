#ifndef SERVERSOCKET_H
#define SERVERSOCKET_H

#include <pthread.h>

#include "ThreadPool/ThreadPool.h"

#include "SocketsUtils.h"
#include "ClientSocket.h"

using namespace std;

void with_server_socket(const int port, void (*action)(ClientSocket sock));

#endif // SERVERSOCKET_H
