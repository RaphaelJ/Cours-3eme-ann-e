#ifndef SOCKETCLIENT_H
#define SOCKETCLIENT_H

#include <arpa/inet.h>
#include <sys/socket.h>
#include <unistd.h>

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
    ~ClientSocket();
    ClientSocket& operator=(const ClientSocket& other);
    
    int getSocketFD() const { return this->_socket; }
    
    // Envoie un vecteur de données
    template <class T>
    inline void send(const vector<T> data)
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
    
    // Envoie une donnée
    template <class T>
    inline void send(const T data)
    {
        if (send(this->_socket, data, sizeof (T), 0) == -1)
            throw SocketException("Erreur lors de l'envoi des données");
    }
    
    // Reçoit un vecteur de données
    template <class T>
    inline vector<T> receive(const size_t n_elems)
    {
        // Recoit dans un buffer
        T *buffer = new T[n_elems];
        
        if (recv(this->_socket, buffer, n_elems *  sizeof (T), 0) == -1)
            throw SocketException("Erreur lors de la réception des données");
        
        // Copie le buffer dans un vector
        vector<T> data(buffer, buffer + n_elems);
        
        return data;
    }
    
    // Reçoit une donnée
    template <class T>
    inline T receive()
    {
        T data;
        
        if (recv(this->_socket, &data, sizeof (T), 0) == -1)
            throw SocketException("Erreur lors de la réception des données");
        
        return data;
    }
    
    void close();    
};

#endif // SOCKETCLIENT_H