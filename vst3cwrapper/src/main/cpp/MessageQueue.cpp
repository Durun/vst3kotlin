#include "MessageQueue.h"

#include <atomic>
#include <cstdio>
#include <cstdlib>

static void enter_lock() {
    auto locked = reinterpret_cast<std::atomic_int *>(&(messageQueue.locked));

    int expected = UNLOCK;
    while (locked->compare_exchange_strong(expected, LOCK) == UNLOCK)
    // if locked=F -> expected=F, locked=T, return=T
    // if locked=T -> expected=T, locked=T, return=F
    {
        // expected=T, locked=T
        expected = UNLOCK;
    }
    // expected=F, locked=T
}

static void exit_lock() {
    auto locked = reinterpret_cast<std::atomic_bool *>(&(messageQueue.locked));
    locked->store(UNLOCK);
}

static int nextIndex(int i) {
    int lastIndex = MessageQueueLength - 1;
    if (i < lastIndex) {
        return i + 1;
    } else {
        return 0;
    }
}

static int min(int x, int y) {
    if (x <= y)
        return x;
    else
        return y;
}

void MessageQueue_init() {
    messageQueue.locked = UNLOCK;
    messageQueue.array = messageQueueArray;
}

void MessageQueue_enqueue(const char data[], int size) {
    // check
    if (size == 0) return;
    if (size < 0) {
        fprintf(stderr, "Illegal argument: size must be 0<= but %d\n", size);
        exit(1);
    }

    // overflow check
    enter_lock();
    if (MessageQueueLength < messageQueue.remainSize + size) {
        fprintf(stderr, "Illegal state: MessageQueue overflow\n");
        exit(1);
    }

    // copy
    for (int i = 0; i < size; i++) {
        int to = (messageQueue.nextWriteIndex + i) % MessageQueueLength;
        messageQueue.array[to] = data[i];
        //fprintf(stderr, "enqueue: %02d <- %c\n", to, data[i]);
    }

    // increment
    messageQueue.nextWriteIndex = (messageQueue.nextWriteIndex + size) % MessageQueueLength;
    messageQueue.remainSize += size;
    exit_lock();
}

void MessageQueue_enqueue_byte(const char data) {
    MessageQueue_enqueue(&data, 1);
}

int MessageQueue_dequeue(/*out*/ char data[], const int readSize) {
    // check
    if (readSize < 0) {
        fprintf(stderr, "Illegal argument: readSize must be 0<= but %d\n", readSize);
        exit(1);
    }

    enter_lock();

    // copy
    auto toRead = min(readSize, messageQueue.remainSize);
    for (int i = 0; i < toRead; i++) {
        int from = (messageQueue.nextReadIndex + i) % MessageQueueLength;
        data[i] = messageQueue.array[from];
        //fprintf(stderr, "Dequeue: %02d -> %c\n", from, data[i]);
    }

    // increment
    messageQueue.nextReadIndex = (messageQueue.nextReadIndex + toRead) % MessageQueueLength;
    messageQueue.remainSize -= toRead;
    exit_lock();
    return toRead;
}
