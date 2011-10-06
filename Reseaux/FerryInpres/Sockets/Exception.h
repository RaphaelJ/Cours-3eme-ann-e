#ifndef EXCEPTION_H
#define EXCEPTION_H

#include <string>

using namespace std;

class Exception
{
private:
    string _message;
    Exception();

public:
    virtual Exception(const char* message);
    virtual Exception(const Exception& other);
    virtual ~Exception();
    virtual Exception& operator=(const Exception& other);
    
    const char* getMessage() const { return this->_message.c_str(); };
};

#endif // EXCEPTION_H
