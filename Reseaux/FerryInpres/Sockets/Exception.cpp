#include "Exception.h"

Exception::Exception(const char* message)
{
    this->_message = message;
}

Exception::Exception(const Exception& other)
{
    this->_message = other.getMessage();
}

Exception::~Exception()
{

}

Exception& Exception::operator=(const Exception& other)
{
    return *this;
}

