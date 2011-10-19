#ifndef EXCEPTION_H
#define EXCEPTION_H

#include <exception>
#include <string>

using namespace std;

class Exception : public exception
{
private:
    string _message;

public:
    Exception(const char* message);
    Exception(const Exception& other);
    virtual ~Exception() throw() {};
    virtual Exception& operator=(const Exception& other);
    
    virtual const char* what() const throw() { return this->_message.c_str(); };
};

#endif // EXCEPTION_H
