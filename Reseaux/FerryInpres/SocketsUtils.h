#ifndef SOCKETSUTILS_H
#define SOCKETSUTILS_H

#include <arpa/inet.h>
#include <sys/socket.h>
#include <unistd.h>

#include "SocketException.h"

int init_socket(const char* ip, const int port)

#endif // SOCKETSUTILS_H
