#include "util/GlobalStore.h"

#include <atomic>
#include <cstdio>
#include <cstdlib>

static void enter_lock(const int index) {
    auto locked = reinterpret_cast<std::atomic_int*>(&(GlobalStore[index].locked));

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

static void exit_lock(const int index) {
    auto locked = reinterpret_cast<std::atomic_bool*>(&(GlobalStore[index].locked));
    locked->store(UNLOCK);
}


void GlobalStore_write(const int index, void* data) {
    //check
    if (index < 0 || GlobalStoreSize <= index) {
        fprintf(stderr, "Illegal argument: index must be in 0-%d but %d\n", GlobalStoreSize - 1, index);
        exit(1);
    }

    enter_lock(index);

    GlobalStore[index].data = data;

    exit_lock(index);
}


void* GlobalStore_read(const int index) {
    //check
    if (index < 0 || GlobalStoreSize <= index) {
        fprintf(stderr, "Illegal argument: index must be in 0-%d but %d\n", GlobalStoreSize - 1, index);
        exit(1);
    }
    enter_lock(index);

    void* ret;
    ret = GlobalStore[index].data;

    exit_lock(index);
    return ret;
}
