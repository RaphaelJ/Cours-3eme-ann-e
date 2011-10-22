#ifndef ASYNC_H
#define ASYNC_H

#include <pthread.h>

void async_call(void *(*routine)(void* arg), void *arg);

#endif // ASYNC_H
