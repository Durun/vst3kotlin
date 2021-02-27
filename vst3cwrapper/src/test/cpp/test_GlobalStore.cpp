#include <cstdio>

#include "assert.h"
#include "util/GlobalStore.h"

int test_GlobalStore() {
    fprintf(stderr, "--test_GlobalStore--\n");

    void* data = (void*)0x12345678L;
    GlobalStore_init(0);
    GlobalStore_write(0, data);
    assert(GlobalStore_read(0) == data);

    GlobalStore_init(GlobalStoreSize - 1);
    GlobalStore_write(GlobalStoreSize - 1, (void*)0xABCDEF);
    assert(GlobalStore_read(GlobalStoreSize - 1) == (void*)0xABCDEF);
    return 0;
}
