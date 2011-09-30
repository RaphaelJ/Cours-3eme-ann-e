#ifndef SOCKETCEXCEPTION_H
#define SOCKETCEXCEPTION_H

#include <string>

class SocketException
{
private:
    string _message;
    SocketException();

public:
    SocketException(const char* message);
    SocketException(const SocketException& other);
    ~SocketException();
    operator=(const SocketException& other);
    
    const char* getMessage() const;
};

#endif // SOCKETCEXCEPTION_H
