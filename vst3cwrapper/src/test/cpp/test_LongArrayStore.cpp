#include <assert.h>

#include <cstdio>

#include "util/LongArrayStore.h"

int test_LongArrayStore() {
    fprintf(stderr, "--test_LongArrayStore--\n");
    auto size = 128;
    auto store = LongArrayStore_alloc(size);
    fprintf(stderr, "Allocated %d length: %p->%p, %p...\n", size, store, &(store->locked), store->array);
    LongArrayStore_init(store, size);
    fprintf(stderr, "Initialized\n");

    LongArrayStore_enter(store);
    fprintf(stderr, "Entered\n");
    LongArrayStore_write(store, 0, 0xABC);
    fprintf(stderr, "Wrote %p\n", 0xABC);
    assert(LongArrayStore_read(store, 0) == 0xABC);
    fprintf(stderr, "Read\n");
    LongArrayStore_exit(store);
    fprintf(stderr, "Exited\n");

    LongArrayStore_delete(store);
    return 0;
}
