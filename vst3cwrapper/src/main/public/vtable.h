#pragma once

#ifdef __cplusplus
extern "C" {
#endif

typedef void (*VirtualFunctionPointer)();

typedef struct VTable {
    int d;
    int i;
    VirtualFunctionPointer func_ptr;
} VTable;

#ifdef __cplusplus
}
#endif
