#include <iostream>

#include "Sockets/Exception.h"
#include "Sockets/ServerSocket.h"
#include "Sockets/ClientSocket.h"

#include "Protocol.h"

int main(int argc, char **argv)
{
    withServerSocket(39001, manageRequest);
}

void manageRequest(ClientSocket sock)
{
    try {
        user_login(sock);
    } catch (Exception e) {
        cout << "Coupure de la connexion au client:" << endl;
        cout << e.getMessage() << endl;
    }
}

void user_login(ClientSocket sock)
{
    protocol packet = sock.receive<protocol>();
    if (packet.type == LOGIN) {
        
        
    } else
        throw Exception("En attente du login, reçu une commande inconnue.");
}

