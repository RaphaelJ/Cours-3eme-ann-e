#ifndef SOCKETCLIENT_H
#define SOCKETCLIENT_H

class SocketClient
{
private:
    int ip;

public:
    SocketClient(int ip);
    SocketClient(const SocketClient& other);
    ~SocketClient();
    SocketClient& operator=(const SocketClient& other);
};

#endif // SOCKETCLIENT_H
