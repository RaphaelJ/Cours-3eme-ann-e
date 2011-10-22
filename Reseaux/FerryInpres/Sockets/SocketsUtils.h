#ifndef SOCKETSUTILS_H
#define SOCKETSUTILS_H

#include <arpa/inet.h>
#include <sys/socket.h>
#include <unistd.h>

#include "SocketException.h"

using namespace std;

namespace socket_utils
{
    enum socket_type { SERVER_SOCKET, CLIENT_SOCKET };
    
    // Initialise un socket. Utilisé par les classes ClientSocket et ServerSocket
    int init_socket(const unsigned int ip, const int port, socket_type type);
    
    // Fonctions POSIX avec gestion des exceptions
    int socket(int domain, int type, int protocol);
    int connect(int _socket, const struct sockaddr *address,
                socklen_t address_len);
    int bind(int _socket, const struct sockaddr *address,
             socklen_t address_len);
    int listen(int _socket, int backlog);
    int accept(int _socket, struct sockaddr *address, socklen_t *address_len);
    ssize_t send(int _socket, const void *buffer, size_t length, int flags);
    ssize_t recv(int _socket, void *buffer, size_t length, int flags);
    int shutdown(int _socket, int how);
    int close(int fildes);
}

#endif // SOCKETSUTILS_H
