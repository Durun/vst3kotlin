#include "MessageQueue.h"
#include "assert.h"
#include "stdio.h"

int test_MessageQueue() {
    char data[4] = {'A', 'B', 'C', 'D'};
    char buf[MessageQueueLength];
    auto read = MessageQueue_dequeue(buf, 4);
    assert(read == 0);

    MessageQueue_enqueue_byte('C');
    read = MessageQueue_dequeue(buf, 0);
    assert(read == 0);
    read = MessageQueue_dequeue(buf, 1);
    assert(read == 1);
    assert(buf[0] == 'C');

    MessageQueue_enqueue_byte('a');
    MessageQueue_enqueue_byte('b');
    MessageQueue_enqueue_byte('c');
    MessageQueue_enqueue_byte('d');

    read = MessageQueue_dequeue(buf, 32);
    assert(read == 4);
    assert(buf[0] == 'a');
    assert(buf[1] == 'b');
    assert(buf[2] == 'c');
    assert(buf[3] == 'd');


    for (int i = 0; i < MessageQueueLength; i++) {
        MessageQueue_enqueue_byte('F');
    }
    read = MessageQueue_dequeue(buf, MessageQueueLength);
    assert(read == MessageQueueLength);
    for (int i = 0; i < MessageQueueLength; i++) {
        assert(buf[i] == 'F');
    }
}
