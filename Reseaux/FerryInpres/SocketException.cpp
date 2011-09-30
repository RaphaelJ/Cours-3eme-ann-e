#include "SocketException.h"

SocketException::SocketException(const char* message)
{
    this._message = message;
}

SocketException::SocketException(const SocketException& other)
{
    this->_message = other.getMessage();
}

SocketException::~SocketException()
{

}

SocketException::operator=(const SocketException& other)
{
    return *this;
}

const char* SocketException::getMessage() const
{
    return this->_message.c_str();
}
