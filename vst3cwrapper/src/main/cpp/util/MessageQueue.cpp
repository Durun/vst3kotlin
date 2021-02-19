#include "util/MessageQueue.h"

#include <cstdio>
#include <cstdlib>

#include "lock.h"
#include "util/ByteQueue.h"

void MessageQueue_init() {
    ByteQueue_init(&messageQueue);
    messageQueue.array = messageQueueArray;
}

void MessageQueue_enqueue(const char data[], int size) {
    ByteQueue_enqueue(&messageQueue, data, size);
}

void MessageQueue_enqueue_byte(const char data) {
    MessageQueue_enqueue(&data, 1);
}

int MessageQueue_dequeue(/*out*/ char data[], const int readSize) {
    return ByteQueue_dequeue(&messageQueue, data, readSize);
}
