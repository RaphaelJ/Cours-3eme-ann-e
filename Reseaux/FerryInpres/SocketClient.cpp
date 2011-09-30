#include "SocketClient.h"

SocketClient::SocketClient()
{
    this->_socket = socket(AF_INET, SOCK_STREAM, 0);
    if (this->_socket == 0)
        throw SocketException("Erreur lors de l'initialisation du socket");
}

SocketClient::SocketClient(const SocketClient& other)
{

}

SocketClient::~SocketClient()
{

}

SocketClient& SocketClient::operator=(const SocketClient& other)
{
    return *this;
}

