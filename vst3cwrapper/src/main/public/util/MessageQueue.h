#pragma once

#include "util/ByteQueue.h"

#ifdef __cplusplus
extern "C" {
#endif

const int MessageQueueLength = 256; // 4KB

static char messageQueueArray[MessageQueueLength] = {};
static ByteQueue messageQueue = {0, messageQueueArray};

void MessageQueue_init();
void MessageQueue_enqueue(const char data[], int size);
void MessageQueue_enqueue_byte(const char data);
int MessageQueue_dequeue(/*out*/ char data[], const int readSize);

#ifdef __cplusplus
}
#endif
