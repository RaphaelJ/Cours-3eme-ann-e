#ifndef SOCKETCLIENT_H
#define SOCKETCLIENT_H

#include <sys/socket.h>

#include "SocketException.h"

class SocketClient
{
private:
    int _socket;

public:
    SocketClient();
    SocketClient(const SocketClient& other);
    ~SocketClient();
    SocketClient& operator=(const SocketClient& other);
    
    void send();
    void receive();
    
    void close();    
};

#endif // SOCKETCLIENT_H
