#pragma once

#ifdef __cplusplus
extern "C" {
#endif


const int MessageQueueLength = 256; // 4KB

const int UNLOCK = 0;
const int LOCK = 1;
typedef struct MessageQueue {
    int locked;  // 0=unlock, 1=lock
    char *array;
    int nextReadIndex;
    int nextWriteIndex;
    int remainSize;
} MessageQueue;

static char messageQueueArray[MessageQueueLength] = {};
static MessageQueue messageQueue = {UNLOCK, messageQueueArray};

void MessageQueue_init();
void MessageQueue_enqueue(const char data[], int size);
void MessageQueue_enqueue_byte(const char data);
int MessageQueue_dequeue(/*out*/ char data[], const int readSize);

#ifdef __cplusplus
}
#endif
