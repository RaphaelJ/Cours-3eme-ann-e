#include <stdlib.h>
#include <string.h>

#include "ClientSocket.h"

// Initialise et connecte la socket à une socket serveur
ClientSocket::ClientSocket(const char* ip, const int port)
{
    this->_socket = init_socket(ip, port);
}

ClientSocket::ClientSocket(const ClientSocket& other)
{
    
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
    close(this->_socket);
}