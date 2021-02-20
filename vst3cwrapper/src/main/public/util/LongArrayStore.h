#pragma once

#ifdef __cplusplus
extern "C" {
#endif

typedef struct LongArrayStore {
    int locked;
    long* array;
} LongArrayStore;

LongArrayStore* LongArrayStore_alloc(int size);
void LongArrayStore_init(LongArrayStore* store, int size);
void LongArrayStore_delete(LongArrayStore* store);
void LongArrayStore_enter(LongArrayStore* store);
void LongArrayStore_exit(LongArrayStore* store);
void LongArrayStore_write(LongArrayStore* store, int index, long data);
long LongArrayStore_read(const LongArrayStore* store, int index);

#ifdef __cplusplus
}
#endif
