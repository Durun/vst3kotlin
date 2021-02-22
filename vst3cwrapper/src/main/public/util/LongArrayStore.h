#pragma once

#include "base/ftypes.h"

#ifdef __cplusplus
extern "C" {
#endif

typedef struct LongArrayStore {
    int locked;
    int64* array;
} LongArrayStore;

LongArrayStore* LongArrayStore_alloc(int size);
void LongArrayStore_init(LongArrayStore* store, int size);
void LongArrayStore_delete(LongArrayStore* store);
void LongArrayStore_enter(LongArrayStore* store);
void LongArrayStore_exit(LongArrayStore* store);
void LongArrayStore_write(LongArrayStore* store, int index, int64 data);
int64 LongArrayStore_read(const LongArrayStore* store, int index);

#ifdef __cplusplus
}
#endif
