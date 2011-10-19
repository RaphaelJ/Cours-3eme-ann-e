#ifndef SOCKETCEXCEPTION_H
#define SOCKETCEXCEPTION_H

#include <string>

#include "Utils/Exception.h"

using namespace std;

class SocketException : Exception
{
public:
    SocketException(const char* message);
    SocketException(const SocketException& other);
    virtual ~SocketException() throw() {};
};

#endif // SOCKETCEXCEPTION_H
