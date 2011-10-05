#include "SocketException.h"

SocketException::SocketException(const char* message)
{
    this->_message = message;
}

SocketException::SocketException(const SocketException& other)
{
    this->_message = other.getMessage();
}

SocketException::~SocketException()
{

}

SocketException& SocketException::operator=(const SocketException& other)
{
    return *this;
}
