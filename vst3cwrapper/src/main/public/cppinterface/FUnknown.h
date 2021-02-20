#pragma once

#include "base/FUID.h"
#include "base/ftypes.h"

#ifdef __cplusplus
extern "C" {
#endif

typedef struct FUnknownVTable {
    tresult PLUGIN_API (*queryInterface)(void *, const TUID, void **);
    uint32 PLUGIN_API (*addRef)(void *);
    uint32 PLUGIN_API (*release)(void *);
} FUnknownVTable;

typedef struct SFUnknown {
    FUnknownVTable *vtable;
} SFUnknown;

#ifdef __cplusplus
}
#endif
