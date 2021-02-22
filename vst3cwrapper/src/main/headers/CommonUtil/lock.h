#pragma once

#ifdef __cplusplus
extern "C" {
#endif

const int UNLOCK = 0;
const int LOCK = 1;

void Lock_enter(int* lock);
void Lock_exit(int* lock);

#ifdef __cplusplus
}
#endif
