#ifndef SOCKETCLIENT_H
#define SOCKETCLIENT_H

#include <arpa/inet.h>
#include <sys/socket.h>
#include <unistd.h>

#include <vector>

#include "SocketsUtils.h"
#include "SocketException.h"

using namespace std;

class ClientSocket
{
private:
    int _socket;

public:
    ClientSocket(const char* ip, const int port);
    ClientSocket(const int socket_fd);
    ClientSocket(const ClientSocket& other);
    ~ClientSocket() {};
    ClientSocket& operator=(const ClientSocket& other);
    
    int get_socket_fd() const { return this->_socket; }
    
    // Envoie une donnée
    template <class T>
    inline ssize_t send(const T data)
    {
        return socket_utils::send(this->_socket, (void *) &data, sizeof (T), 0);
    }
    
    // Envoie un vecteur de données
    inline ssize_t send(const void *data, size_t length)
    {
	return socket_utils::send(this->_socket, data, length, 0);
    }
    
    // Reçoit une donnée
    template <class T>
    inline ssize_t receive(T *data)
    {   
        ssize_t ret = socket_utils::recv(this->_socket, data, sizeof (T), 0);
        
        if (ret != sizeof (T))
            throw SocketException("Réception incomplète des données");
        
        return ret;
    }
    
    // Reçoit un vecteur de données
    template <class T>
    inline ssize_t receive(void *data, size_t length)
    {        
        return socket_utils::recv(this->_socket, data, length, 0);
    }
    
    void close();    
};

#endif // SOCKETCLIENT_H