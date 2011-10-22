#include "Server.h"

ferry docked_ferries[] = { 
    { 3, ferry::DOCKED }, { 0, ferry::NO_FERRY }, { 17, ferry::DOCKED },
    { 2, ferry::DOCKED }, { 3, ferry::DOCKED }
};

int main(void)
{
    pthread_mutex_init(&mutex_waiting, NULL);
    pthread_mutex_init(&mutex_leaving, NULL);
    
    async_call(terminal_server, NULL);
    
    return 0;
}