#pragma once

#include "util/ByteQueue.h"

#ifdef __cplusplus
extern "C" {
#endif

const int MessageQueueLength = 256; // 4KB

static char8 messageQueueArray[MessageQueueLength] = {};
static ByteQueue messageQueue = {0, messageQueueArray};

void MessageQueue_init();
void MessageQueue_enqueue(const char8 data[], int size);
void MessageQueue_enqueue_byte(char8 data);
int MessageQueue_dequeue(/*out*/ char8 data[], int readSize);

#ifdef __cplusplus
}
#endif
