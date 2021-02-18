#include "MessageQueue.h"
#include "assert.h"
#include "stdio.h"

int test_MessageQueue() {
    MessageQueueEntry data[4] = {1L, 2L, 3L, 4L};
    MessageQueueEntry buf[MessageQueueLength];
    auto read = MessageQueue_dequeue(buf, 4);
    assert(read == 0);

    MessageQueue_enqueue(&(data[3]));
    read = MessageQueue_dequeue(buf, 0);
    assert(read == 0);
    read = MessageQueue_dequeue(buf, 1);
    assert(read == 1);
    assert(buf[0].data1 == 4L);

    MessageQueue_enqueue(&(data[0]));
    MessageQueue_enqueue(&(data[1]));
    MessageQueue_enqueue(&(data[2]));
    MessageQueue_enqueue(&(data[3]));

    read = MessageQueue_dequeue(buf, 32);
    assert(read == 4);
    assert(buf[0].data1 == 1L);
    assert(buf[1].data1 == 2L);
    assert(buf[2].data1 == 3L);
    assert(buf[3].data1 == 4L);

    MessageQueueEntry fillData = {114514L};
    for (int i = 0; i < MessageQueueLength; i++) {
        MessageQueue_enqueue(&fillData);
    }
    read = MessageQueue_dequeue(buf, MessageQueueLength);
    assert(read == MessageQueueLength);
    for (int i = 0; i < MessageQueueLength; i++) {
        assert(buf[i].data1 == fillData.data1);
    }
}
