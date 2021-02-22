#include "util/ByteQueue.h"

#include <cstdio>
#include <cstdlib>

#include "CommonUtil/lock.h"

using namespace CommonUtil;

static void enter_lock(ByteQueue *queue) {
    Lock_enter(&(queue->locked));
}

static void exit_lock(ByteQueue *queue) {
    Lock_exit(&(queue->locked));
}

static int min(int x, int y) {
    if (x <= y)
        return x;
    else
        return y;
}

ByteQueue* ByteQueue_alloc() {
    auto queue = reinterpret_cast<ByteQueue*>(malloc(sizeof(ByteQueue)));
    if (queue == nullptr) {
        fprintf(stderr, "Failed to allocate ByteQueue.");
    }
    auto array = reinterpret_cast<char*>(malloc(sizeof(char) * ByteQueueLength));
    if (array == nullptr) {
        fprintf(stderr, "Failed to allocate ByteQueue (size = %d bytes)", ByteQueueLength);
    }
    queue->array = array;
    return queue;
}

void ByteQueue_init(ByteQueue* queue) {
    queue->nextReadIndex = 0;
    queue->nextWriteIndex = 0;
    queue->remainSize = 0;
    queue->locked = UNLOCK;
}

void ByteQueue_free(ByteQueue* queue) {
    free(queue->array);
    free(queue);
}
void ByteQueue_enqueue(ByteQueue* queue, const char data[], int size) {
    // check
    if (size == 0) return;
    if (size < 0) {
        fprintf(stderr, "Illegal argument: size must be 0<= but %d\n", size);
        exit(1);
    }

    // overflow check
    enter_lock(queue);
    if (ByteQueueLength < queue->remainSize + size) {
        fprintf(stderr, "Illegal state: MessageQueue overflow\n");
        exit_lock(queue);
        exit(1);
    }

    // copy
    for (int i = 0; i < size; i++) {
        int to = (queue->nextWriteIndex + i) % ByteQueueLength;
        queue->array[to] = data[i];
        //fprintf(stderr, "enqueue: %02d <- %c\n", to, data[i]);
    }

    // increment
    queue->nextWriteIndex = (queue->nextWriteIndex + size) % ByteQueueLength;
    queue->remainSize += size;
    exit_lock(queue);
}
int ByteQueue_dequeue(ByteQueue* queue, /*out*/ char data[], const int readSize) {
    // check
    if (readSize < 0) {
        fprintf(stderr, "Illegal argument: readSize must be 0<= but %d\n", readSize);
        exit(1);
    }

    enter_lock(queue);

    // copy
    auto toRead = min(readSize, queue->remainSize);
    for (int i = 0; i < toRead; i++) {
        int from = (queue->nextReadIndex + i) % ByteQueueLength;
        data[i] = queue->array[from];
        //fprintf(stderr, "Dequeue(%x): %02d -> %c\n", queue, from, data[i]);
    }

    // increment
    queue->nextReadIndex = (queue->nextReadIndex + toRead) % ByteQueueLength;
    queue->remainSize -= toRead;
    exit_lock(queue);
    return toRead;
}
