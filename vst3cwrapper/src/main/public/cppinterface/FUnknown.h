#pragma once

#include "base/FUID.h"
#include "base/ftypes.h"

#ifdef __cplusplus
extern "C" {
#endif

typedef struct FUnknownVTable {
    tresult (*queryInterface)(void *, const TUID, void **);
    uint32 (*addRef)(void *);
    uint32 (*release)(void *);
} FUnknownVTable;

typedef struct SFUnknown {
    FUnknownVTable *vtable;
} SFUnknown;

#ifdef __cplusplus
}
#endif
