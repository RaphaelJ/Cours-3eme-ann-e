#include "Async.h"

// Exécute le routine d'un un nouveau thread et rends la main
// (effectue un appel nom bloquant).
void async_call(void *(*routine)(void* arg), void *arg)
{
    pthread_t t;
    pthread_create(&t, NULL, routine, arg);
    pthread_detach(t);
}
