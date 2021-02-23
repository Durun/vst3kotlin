#pragma once

#include "FUnknown.h"
#include "base/PClassInfo.h"
#include "base/PFactoryInfo.h"

#ifdef __cplusplus
extern "C" {
#endif

typedef struct IPluginFactoryVTable {
    FUnknownVTable FUnknown;
    tresult (*getFactoryInfo)(void *, PFactoryInfo *);
    int32 (*countClasses)(void *);
    tresult (*getClassInfo)(void *, int32, PClassInfo *);
    tresult (*createInstance)(void *, FIDString, FIDString, void **);
} IPluginFactoryVTable;

typedef struct SIPluginFactory {
    IPluginFactoryVTable *vtable;
} SIPluginFactory;

#ifdef __cplusplus
}
#endif
