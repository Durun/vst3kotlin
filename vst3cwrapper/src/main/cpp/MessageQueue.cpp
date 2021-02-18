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

static void copy(const MessageQueueEntry *from, MessageQueueEntry *to) {
    to->data1 = from->data1;
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

void MessageQueue_enqueue(const MessageQueueEntry *data) {
    enter_lock();
    // overflow check
    if (MessageQueueLength <= messageQueue.remainSize) {
        fprintf(stderr, "Illegal state: MessageQueue overflow\n");
        exit(1);
    }

    // copy
    auto to = &(messageQueue.array[messageQueue.nextWriteIndex]);
    copy(data, to);

    // increment
    messageQueue.nextWriteIndex = nextIndex(messageQueue.nextWriteIndex);
    messageQueue.remainSize++;
    exit_lock();
}

void MessageQueue_enqueue_long(long data) {
    auto ptr = reinterpret_cast<const MessageQueueEntry*>(&data);
    MessageQueue_enqueue(ptr);
}

int MessageQueue_dequeue(/*out*/ MessageQueueEntry data[], const int readSize) {
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
        copy(&(messageQueue.array[from]), &(data[i]));
    }

    // increment
    messageQueue.nextReadIndex = (messageQueue.nextReadIndex + toRead) % MessageQueueLength;
    messageQueue.remainSize -= toRead;
    exit_lock();
    return toRead;
}
