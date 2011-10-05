#include "ServerSocket.h"

void manageClient(int client_fd, void (*action)(ClientSocket sock));
void* threadRoutine(void *v_args);

// Lance un serveur écoutant sur un port et gérant les connexions sur
// plusieurs threads.
void withServerSocket(const int port, void (*action)(ClientSocket sock))
{
    int _socket = utils::init_socket(INADDR_ANY, port, utils::SERVER_SOCKET);
    
    utils::listen(_socket, 0);
    
    for (;;)
        manageClient(utils::accept(_socket, NULL, NULL), action);
    
    shutdown(_socket, SHUT_RDWR);
    close(_socket);
}

// Structure comportant l'action du serveur à exécuter dans le nouveau thread
// et le socket associé.
typedef struct _thread_args {
    void (*action)(ClientSocket sock);
    int client_fd;
} thread_args;

// Lance la gestion du client dans un nouveau thread.
void manageClient(int client_fd, void (*action)(ClientSocket sock))
{
    pthread_t t;
    
    thread_args *args = new thread_args;
    args->action = action;
    args->client_fd = client_fd;
    
    pthread_create(&t, NULL, threadRoutine, (void*) args);
}

// Appelle l'action de serveur depuis le nouveau thread
void* threadRoutine(void *v_args)
{
    thread_args *args = (thread_args*) v_args;
    
    args->action(ClientSocket(args->client_fd));
    
    delete args;
    pthread_exit(NULL);
}