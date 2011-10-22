#include "Time.h"

// Convertit une chaine au format xx:xx en temps C
// const pour pouvoir être utilisé depuis string.c_str()
s_time str_to_time(const char *const_str_time)
{
    s_time ret;
    char str_time[6];
    
    strcpy(str_time, const_str_time); 
    
    str_time[2] = '\0';
    ret.hour = atoi(str_time);
    ret.min = atoi(str_time + 3);
    
    return ret;
}

void time_to_str(const s_time t, char *str_time)
{
    sprintf(str_time, "%hd:%hd", t.hour, t.min);
}

s_time current_time()
{
    time_t t = time(NULL);
    struct tm *tm = localtime(&t);
    s_time ret;
    
    ret.hour = tm->tm_hour;
    ret.min = tm->tm_min;
    
    return ret;
}

// Retourne le nombre de minutes entre deux heures.
// La seconde heure est toujours après la première (le jour même ou le
// lendemain)
short time_span(s_time t1, s_time t2)
{
    short diff = 0;
    
    if (t1.hour > t2.hour || (t1.hour == t2.hour && t1.min > t2.min)) {
        // t2 se produit le jour suivant
        diff += 24 * 60;
    } 
    
    return diff + (t2.hour - t1.hour) * 60 + (t2.min - t1.min);
}