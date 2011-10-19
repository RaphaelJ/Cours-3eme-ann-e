#include <iostream>

#include "IniParser/IniParser.h"

#include "ServerSocket.h"
#include "ClientSocket.h"

int main(int argc, char **argv)
{
    IniParser f("test.csv");
    cout << f.getValue("hello");
    
    return EXIT_SUCCESS;
}
