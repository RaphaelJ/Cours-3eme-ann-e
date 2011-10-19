#ifndef THREADPOOL_H
#define THREADPOOL_H

#include <pthread.h>
#include <semaphore.h>

#include <list>
#include <queue>

using namespace std;

typedef struct _routine {
    void (*fct)(void *);
    void *arg;
} routine;

class ThreadPool
{
private:    
    list<pthread_t> _threads;
    queue<routine> _waiting; // Routines en attente d'exécution
    
    // Verrouille et patiente sur la liste des routines en attente
    sem_t _sem_waiting;
    pthread_mutex_t _mutex_waiting;

    ThreadPool(const ThreadPool& other) {};
    
public:
    ThreadPool(int n_threads);
    virtual ~ThreadPool();
    virtual ThreadPool& operator=(const ThreadPool& other);
    
    void inject(void (*fct)(void *), void *arg);
};

#endif // THREADPOOL_H