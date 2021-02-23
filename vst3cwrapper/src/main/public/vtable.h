#pragma once

#ifdef __cplusplus
extern "C" {
#endif

typedef struct VTable {
    void* func_ptr;
} VTable;

#ifdef __cplusplus
}
#endif
