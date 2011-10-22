#ifndef TIME_H
#define TIME_H

#include <stdlib.h>
#include <stdio.h>
#include <time.h>

typedef struct _s_time {
    short hour;
    short min;
} s_time;

s_time str_to_time(char str_time[]);
void time_to_str(s_time t, char str_time);
s_time current_time();
short time_span(s_time t1, s_time t2);

#endif // TIME_H
