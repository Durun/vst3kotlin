#pragma once

#ifdef __cplusplus
extern "C" {
#endif

typedef struct LongStoreEntry {
    int locked;  // 0=unlock, 1=lock
    long data;
} LongStoreEntry;

LongStoreEntry* LongStore_new();
void LongStore_delete(LongStoreEntry* entry);
void LongStore_enter(LongStoreEntry* entry);
void LongStore_exit(LongStoreEntry* entry);
void LongStore_write(LongStoreEntry* entry, long data);
long LongStore_read(const LongStoreEntry* entry);

#ifdef __cplusplus
}
#endif
