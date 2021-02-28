#include "util/GlobalStore.h"

#include <cstdio>
#include <cstdlib>

#include "CommonUtil/lock.h"

using namespace CommonUtil;

static void enter_lock(const int index) {
    Lock_enter(&(GlobalStore[index].locked));
}

static void exit_lock(const int index) {
    Lock_exit(&(GlobalStore[index].locked));
}

void GlobalStore_init(int index) {
    GlobalStore[index].locked = UNLOCK;
}

void GlobalStore_write(const int index, void *data) {
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
