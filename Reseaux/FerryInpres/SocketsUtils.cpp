#include "SocketsUtils.h"

// Initialise un socket. Utilisé par les classes ClientSocket et ServerSocket
int init_socket(const char* ip, const int port)
{
    int _socket;
    _socket = socket(AF_INET, SOCK_STREAM, 0);
    if (_socket == -1)
        throw SocketException("Erreur lors de l'initialisation de la socket");
    
    struct sockaddr_in dest = {0};
    dest.sin_addr = ip;
    dest.sin_port = htons(port);
    dest.sin_family = AF_INET;
    if (connect(_socket, &((struct sockaddr) dest), sizeof (struct sockaddr)) == -1)
        throw SocketException("Erreur lors de la connextion de la socket");
}