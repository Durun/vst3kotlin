
#include <assert.h>
#include <cstdio>

#include "util/LongStore.h"

int test_LongStore() {
    fprintf(stderr, "--test_LongStore--\n");

    auto entry1 = LongStore_new();
    fprintf(stderr, "New : %x\n", entry1);

    LongStore_enter(entry1);

    auto entry2 = LongStore_new();
    fprintf(stderr, "New : %x\n", entry2);
    LongStore_enter(entry2);

	LongStore_write(entry1, 0xABCD);

	LongStore_write(entry2, 0xEF12);

    LongStore_exit(entry1);

    LongStore_enter(entry1);
    assert(LongStore_read(entry1) == 0xABCD);
    LongStore_exit(entry1);
    LongStore_delete(entry1);
    fprintf(stderr, "Delete : %x\n", entry1);

    assert(LongStore_read(entry2) == 0xEF12);
    LongStore_exit(entry2);
    LongStore_delete(entry2);
    fprintf(stderr, "Delete : %x\n", entry2);
}
