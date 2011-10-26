#include <stdlib.h>
#include <string.h>

#include "ClientSocket.h"

// Mutex verrouillant les accès réseau
pthread_mutex_t mutex_pause;

// Initialise et connecte la socket à une socket serveur
ClientSocket::ClientSocket(const char* ip, const int port)
{
    unsigned int ip_bin;
    inet_pton(AF_INET, ip, &ip_bin);
    this->_socket = socket_utils::init_socket(ip_bin, port, socket_utils::CLIENT_SOCKET);
}

// Initialise l'object socket à partir d'un descripteur de socket existant
ClientSocket::ClientSocket(const int socket_fd)
{
    this->_socket = socket_fd;
}

ClientSocket::ClientSocket(const ClientSocket& other)
{
    this->_socket = other.get_socket_fd();
}

ClientSocket& ClientSocket::operator=(const ClientSocket& other)
{
    return *this;
}

void ClientSocket::close()
{
    socket_utils::shutdown(this->_socket, SHUT_RDWR);
    socket_utils::close(this->_socket);
}