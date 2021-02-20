#pragma once

#ifdef __cplusplus
extern "C" {
#endif

const int ByteQueueLength = 4096;  // 4KB

typedef struct ByteQueue {
    int locked;  // 0=unlock, 1=lock
    char* array;
    int nextReadIndex;
    int nextWriteIndex;
    int remainSize;
} ByteQueue;

ByteQueue* ByteQueue_alloc();
void ByteQueue_init(ByteQueue* queue);
void ByteQueue_free(ByteQueue* queue);
void ByteQueue_enqueue(ByteQueue* queue, const char data[], int size);
int ByteQueue_dequeue(ByteQueue* queue, /*out*/ char data[], const int readSize);

#ifdef __cplusplus
}
#endif
