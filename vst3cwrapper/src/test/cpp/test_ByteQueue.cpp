#include "assert.h"
#include "stdio.h"
#include "util/ByteQueue.h"

int test_ByteQueue() {
    auto queue = ByteQueue_alloc();
    ByteQueue_init(queue);

    char buf[ByteQueueLength];
    auto read = ByteQueue_dequeue(queue, buf, 4);
    assert(read == 0);

    ByteQueue_enqueue(queue, "ABC", 3);

    read = ByteQueue_dequeue(queue, buf, 0);
    assert(read == 0);

    read = ByteQueue_dequeue(queue, buf, 1);
    assert(read == 1);
    assert(buf[0] == 'A');

    auto queue2 = ByteQueue_alloc();
    ByteQueue_init(queue2);
    ByteQueue_enqueue(queue2, "12345", 4);

    read = ByteQueue_dequeue(queue, buf, 3);
    assert(read == 2);
    assert(buf[0] == 'B');
    assert(buf[1] == 'C');

    for (int i = 0; i < ByteQueueLength; i++) {
        buf[i] = 'F';
    }
    ByteQueue_enqueue(queue, buf, ByteQueueLength);
    for (int i = 0; i < ByteQueueLength; i++) {
        buf[i] = '0';
    }
    read = ByteQueue_dequeue(queue, buf, ByteQueueLength);
    assert(read == ByteQueueLength);
    for (int i = 0; i < ByteQueueLength; i++) {
        assert(buf[i] == 'F');
    }

    ByteQueue_dequeue(queue2, buf, 5);
    assert(buf[3] == '4');

    ByteQueue_free(queue);
    ByteQueue_free(queue2);
}
