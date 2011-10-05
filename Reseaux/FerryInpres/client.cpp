#include <iostream>

#include "ServerSocket.h"
#include "ClientSocket.h"

int main(int argc, char **argv)
{
    withServerSocket(39001, manageRequest);
}
