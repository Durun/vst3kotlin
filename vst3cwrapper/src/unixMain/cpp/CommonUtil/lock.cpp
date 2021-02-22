#include "CommonUtil/lock.h"

#include <ctime>
#include <unistd.h>
#include <atomic>
#include <cstdio>
#include <cstdlib>

static const int waitTime = 10;  //microseconds
static const int timeout = 3;  // seconds

void CommonUtil::Lock_enter(int *lock) {
    auto locked = reinterpret_cast<std::atomic_int *>(lock);

    int expected = CommonUtil::UNLOCK;
    time_t startTime = 0;
    while (locked->compare_exchange_strong(expected, CommonUtil::LOCK) == CommonUtil::UNLOCK)
        // if locked=F -> expected=F, locked=T, return=T
        // if locked=T -> expected=T, locked=T, return=F
    {
        // expected=T, locked=T
        expected = CommonUtil::UNLOCK;

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
        if (timeout <= duration) {
            fprintf(stderr, "Lock Timeout %ds: %p\n", timeout, lock);
            exit(1);
        }

        // sleep
        usleep(waitTime);
    }
    // expected=F, locked=T
    //fprintf(stderr, "Enter lock: %x\n", lock);
}

void CommonUtil::Lock_exit(int *lock) {
    auto locked = reinterpret_cast<std::atomic_bool *>(lock);
    locked->store(CommonUtil::UNLOCK);
    //fprintf(stderr, "Exit lock: %x\n", lock);
}
