#include "assert.h"
#include "util/GlobalStore.h"

int test_GlobalStore() {
    void* data = (void*)0x12345678L;
    GlobalStore_write(0, data);
    assert(GlobalStore_read(0) == data);

    GlobalStore_write(GlobalStoreSize - 1, (void*)0xABCDEF);
    assert(GlobalStore_read(GlobalStoreSize - 1) == (void*)0xABCDEF);
}
