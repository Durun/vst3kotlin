#pragma once

#ifdef __cplusplus
extern "C" {
#endif


const int MessageQueueLength = 4;

typedef struct MessageQueueEntry {
    long data1;
} MessageQueueEntry;

const int UNLOCK = 0;
const int LOCK = 1;
typedef struct MessageQueue {
    int locked;  // 0=unlock, 1=lock
    MessageQueueEntry *array;
    int nextReadIndex;
    int nextWriteIndex;
    int remainSize;
} MessageQueue;

static MessageQueueEntry messageQueueArray[MessageQueueLength] = {};
static MessageQueue messageQueue = {UNLOCK, messageQueueArray};

void MessageQueue_init();
void MessageQueue_enqueue(const MessageQueueEntry *data);
int MessageQueue_dequeue(/*out*/ MessageQueueEntry data[], const int readSize);

#ifdef __cplusplus
}
#endif
