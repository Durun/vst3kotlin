#pragma once


#ifdef __cplusplus
extern "C" {
#endif

const int GlobalStoreSize = 256; // bytes

typedef struct GlobalStoreEntry {
    int locked;  // 0=unlock, 1=lock
    void* data;
} GlobalStoreEntry;

static GlobalStoreEntry GlobalStore[GlobalStoreSize] = {};

void GlobalStore_init(int index);
void GlobalStore_write(int index, void* data);
void* GlobalStore_read(int index);

#ifdef __cplusplus
}
#endif
