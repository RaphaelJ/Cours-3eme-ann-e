#include <iostream>

#include "IniParser/IniParser.h"

#include "Sockets/ClientSocket.h"

int main(int argc, char **argv)
{
    IniParser f("test.csv");
    cout << f.get_value("hello");
    
    return EXIT_SUCCESS;
}