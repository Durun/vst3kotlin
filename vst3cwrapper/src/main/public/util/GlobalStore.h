#pragma once

#include "lock.h"

#ifdef __cplusplus
extern "C" {
#endif

const int GlobalStoreSize = 256; // bytes

typedef struct GlobalStoreEntry {
    int locked;  // 0=unlock, 1=lock
    void* data;
} GlobalStoreEntry;

static GlobalStoreEntry GlobalStore[GlobalStoreSize] = {};

void GlobalStore_write(const int index, void* data);
void* GlobalStore_read(const int index);

#ifdef __cplusplus
}
#endif
