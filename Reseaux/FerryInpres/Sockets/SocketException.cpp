#include "SocketException.h"

SocketException::SocketException(const char* message): Exception(message)
{
}

SocketException::SocketException(const SocketException& other): Exception(other)
{
}

SocketException::~SocketException() : ~Exception()
{
}
