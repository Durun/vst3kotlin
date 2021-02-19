#include "lock.h"

#include <atomic>

void Lock_enter(int* lock) {
    auto locked = reinterpret_cast<std::atomic_int*>(lock);

    int expected = UNLOCK;
    while (locked->compare_exchange_strong(expected, LOCK) == UNLOCK)
    // if locked=F -> expected=F, locked=T, return=T
    // if locked=T -> expected=T, locked=T, return=F
    {
        // expected=T, locked=T
        expected = UNLOCK;
    }
    // expected=F, locked=T
}

void Lock_exit(int* lock) {
    auto locked = reinterpret_cast<std::atomic_bool*>(lock);
    locked->store(UNLOCK);
}
