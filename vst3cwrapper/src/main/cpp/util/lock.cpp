#include "lock.h"

#include <time.h>
#include <unistd.h>

#include <atomic>
#include <cstdio>
#include <cstdlib>

static const int waitTime = 10;  //microseconds
static const long tickPerSecond = 1000000 / waitTime;
static const int timeout = 3;  // seconds

void Lock_enter(int* lock) {
    auto locked = reinterpret_cast<std::atomic_int*>(lock);

    int expected = UNLOCK;
    time_t startTime = 0;
    while (locked->compare_exchange_strong(expected, LOCK) == UNLOCK)
    // if locked=F -> expected=F, locked=T, return=T
    // if locked=T -> expected=T, locked=T, return=F
    {
        // expected=T, locked=T
        expected = UNLOCK;

        // timer start
        time_t now;
        time(&now);
        if (startTime == 0) {
            // fprintf(stderr, "Waiting Lock %ds: %x\n", timeout, lock);
            startTime = now;
        }

        // timer
        auto duration = now - startTime;

        // timer stop
        if (timeout <= now - startTime) {
            fprintf(stderr, "Lock Timeout %ds: %x\n", timeout, lock);
            exit(1);
        }

        // sleep
        usleep(waitTime);
    }
    // expected=F, locked=T
    //fprintf(stderr, "Enter lock: %x\n", lock);
}

void Lock_exit(int* lock) {
    auto locked = reinterpret_cast<std::atomic_bool*>(lock);
    locked->store(UNLOCK);
    //fprintf(stderr, "Exit lock: %x\n", lock);
}
