#ifndef SERVERSOCKET_H
#define SERVERSOCKET_H

#include "SocketsUtils.h"
#include "ClientSocket.h"

class ServerSocket
{
private:
    

public:
    ServerSocket();
    ServerSocket(const ServerSocket& other);
    virtual ~ServerSocket();
    virtual ServerSocket& operator=(const ServerSocket& other);
    virtual bool operator==(const ServerSocket& other) const;
};

#endif // SERVERSOCKET_H
