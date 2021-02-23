#pragma once

#include "base/ftypes.h"

#ifdef __cplusplus
extern "C" {
#endif

const int ByteQueueLength = 4096;  // 4KB

typedef struct ByteQueue {
    int locked;  // 0=unlock, 1=lock
    char8* array;
    int nextReadIndex;
    int nextWriteIndex;
    int remainSize;
} ByteQueue;

ByteQueue* ByteQueue_alloc();
void ByteQueue_init(ByteQueue* queue);
void ByteQueue_free(ByteQueue* queue);
void ByteQueue_enqueue(ByteQueue* queue, const char8 data[], int size);
int ByteQueue_dequeue(ByteQueue* queue, /*out*/ char8 data[], int readSize);

#ifdef __cplusplus
}
#endif
