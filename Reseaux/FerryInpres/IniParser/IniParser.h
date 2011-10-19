#ifndef INIPARSER_H
#define INIPARSER_H

#include <stdlib.h>
#include <stdio.h>
#include <string.h>

#include <fstream>
#include <map>
#include <string>

#include "Utils/Exception.h"

#define BUFFER_LEN 255

using namespace std;

class IniParser
{
private:
    map<string, string> _assoc;

public:
    IniParser(const char* path);
    IniParser(const IniParser& other);
    virtual ~IniParser() {};
    virtual IniParser& operator=(const IniParser& other);
    
    string get_value(string key) const;
    map<string, string> get_assoc() const { return this->_assoc; };
};

#endif // INIPARSER_H
