#include <stdlib.h>
#include <string.h>

#include "ClientSocket.h"

// Initialise et connecte la socket à une socket serveur
ClientSocket::ClientSocket(const char* ip, const int port)
{
    unsigned int ip_bin;
    inet_pton(AF_INET, ip, &ip_bin);
    this->_socket = utils::init_socket(ip_bin, port, utils::CLIENT_SOCKET);
}

// Initialise l'object socket à partir d'un descripteur de socket existant
ClientSocket::ClientSocket(const int socket_fd)
{
    this->_socket = socket_fd;
}

ClientSocket::ClientSocket(const ClientSocket& other)
{
    this->_socket = other.getSocketFD();
}

ClientSocket::~ClientSocket()
{

}

ClientSocket& ClientSocket::operator=(const ClientSocket& other)
{
    return *this;
}

void ClientSocket::close()
{
    utils::shutdown(this->_socket, SHUT_RDWR);
    utils::close(this->_socket);
}