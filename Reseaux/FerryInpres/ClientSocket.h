#ifndef SOCKETCLIENT_H
#define SOCKETCLIENT_H

#include <arpa/inet.h>
#include <sys/socket.h>
#include <unistd.h>

#include "SocketsUtils.h"
#include "SocketException.h"

class ClientSocket
{
private:
    int _socket;

public:
    ClientSocket(const char* ip, const int port);
    ClientSocket(const ClientSocket& other);
    ~ClientSocket();
    ClientSocket& operator=(const ClientSocket& other);
    
    template <class T>
    inline void ClientSocket::send(const vector<T> data)
    {
        // Copie le contenu du vecteur dans un buffer
        T *buffer = new T[data.size()];
        size_t length = data.size() * sizeof (T);
        
        for (int i = 0; i < data.size(); i++) 
            buffer[i] = data[i];
        
        // Envoie le buffer
        if (send(this->_socket, buffer, length, 0) == -1)
            throw SocketException("Erreur lors de l'envoi des données");
    }

    template <class T>
    inline vector<T> ClientSocket::receive(const size_t n_elems)
    {
        // Recoit dans un buffer
        T *buffer = new T[n_elems];
        
        if (recv(this->_socket, buffer, n_elems *  sizeof (T), 0) == -1)
            throw SocketException("Erreur lors de la réception des données");
        
        // Copie le buffer dans un vector
        vector<T> data(buffer, buffer + n_elems);
        
        return data;
    }
    
    void close();    
};

#endif // SOCKETCLIENT_H