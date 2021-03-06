#include "util/LongArrayStore.h"

#include <cstdio>
#include <cstdlib>

#include "CommonUtil/lock.h"

using namespace CommonUtil;

LongArrayStore *LongArrayStore_alloc(int size) {
    auto store = reinterpret_cast<LongArrayStore *>(malloc(sizeof(LongArrayStore)));
    if (store == nullptr) {
        fprintf(stderr, "Failed to allocate LongArrayStore.");
    }
    auto array = reinterpret_cast<int64 *>(malloc(sizeof(int64) * size));
    if (array == nullptr) {
        fprintf(stderr, "Failed to allocate ByteQueue (size = %d bytes)", size);
    }
    store->array = array;
    return store;
}
void LongArrayStore_init(LongArrayStore* store, int size) {
    for (int i = 0; i < size; i++) {
        store->array[i] = 0L;
    }
    store->locked = UNLOCK;
}
void LongArrayStore_delete(LongArrayStore* store) {
    free(store->array);
    free(store);
}
void LongArrayStore_enter(LongArrayStore* store) {
    Lock_enter(&(store->locked));
}
void LongArrayStore_exit(LongArrayStore* store) {
    Lock_exit(&(store->locked));
}
void LongArrayStore_write(LongArrayStore* store, int index, int64 data) {
    store->array[index] = data;
}
int64 LongArrayStore_read(const LongArrayStore* store, int index) {
    return store->array[index];
}
