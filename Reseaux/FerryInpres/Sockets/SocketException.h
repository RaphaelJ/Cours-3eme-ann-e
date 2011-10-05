#ifndef SOCKETCEXCEPTION_H
#define SOCKETCEXCEPTION_H

#include <string>
#include <vector>

using namespace std;

class SocketException
{
private:
    string _message;
    SocketException();

public:
    SocketException(const char* message);
    SocketException(const SocketException& other);
    ~SocketException();
    SocketException& operator=(const SocketException& other);
    
    const char* getMessage() const { return this->_message.c_str(); };
};

#endif // SOCKETCEXCEPTION_H
