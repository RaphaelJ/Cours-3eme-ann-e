#include <iostream>

#include "ServerSocket.h"
#include "ClientSocket.h"

int main(int argc, char **argv)
{
    withServerSocket(39001, manageRequest);
}

void manageRequest(ClientSocket sock)
{
    vector<char> data();
    data[0] = 'h';
    data[1] = 'e';
    data[2] = 'l';
    data[3] = 'l';
    data[4] = 'o';
    
    sock.send<char>(data);
}