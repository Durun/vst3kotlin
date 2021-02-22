#include "util/LongStore.h"

#include <cstdio>
#include <cstdlib>

#include "CommonUtil/lock.h"

using namespace CommonUtil;

LongStoreEntry *LongStore_alloc() {
    auto ptr = malloc(sizeof(LongStoreEntry));
    if (ptr == nullptr) {
        fprintf(stderr, "Failed to allocate LongStoreEntry.");
    }
    auto entry = reinterpret_cast<LongStoreEntry *>(ptr);
    return entry;
}

void LongStore_init(LongStoreEntry *entry) {
    entry->data = 0L;
    entry->locked = UNLOCK;
}
void LongStore_delete(LongStoreEntry* entry) {
    Lock_enter(&(entry->locked));
    free(entry);
}
void LongStore_enter(LongStoreEntry* entry) {
    Lock_enter(&(entry->locked));
}

void LongStore_exit(LongStoreEntry* entry) {
    Lock_exit(&(entry->locked));
}

void LongStore_write(LongStoreEntry* entry, long data) {
    entry->data = data;
}

long LongStore_read(const LongStoreEntry* entry) {
    return entry->data;
}
