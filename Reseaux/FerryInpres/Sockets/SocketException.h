#ifndef SOCKETCEXCEPTION_H
#define SOCKETCEXCEPTION_H

#include <string>

#include "Exception.h"

using namespace std;

class SocketException : Exception
{
public:
    virtual SocketException(const char* message);
    virtual SocketException(const SocketException& other);
    virtual ~SocketException();
};

#endif // SOCKETCEXCEPTION_H
