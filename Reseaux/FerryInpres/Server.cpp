#include "Server.h"

// Vecteur donnant le numero du ferry présent dans chaque terminal
// (0 si aucun ferry)
int docked_ferries[] = { 1, 4, 0, 3, 7, 0 };

// Ferrys en attente de traitement dans l'un des terminaux
queue<int> waiting_ferries;
pthread_mutex_t mutex_waiting;

// Ferrys en cours de départ
list<int> leaving_ferries;
pthread_mutex_t mutex_leaving;

int main(void)
{
    pthread_mutex_init(&mutex_waiting, NULL);
    pthread_mutex_init(&mutex_leaving, NULL);
    
    terminal_server(NULL);
    
    return 0;
}