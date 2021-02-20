#pragma once

#include "FUnknown.h"
#include "base/PClassInfo.h"
#include "base/PFactoryInfo.h"

#ifdef __cplusplus
extern "C" {
#endif

typedef struct IPluginFactoryVTable {
    FUnknownVTable FUnknown;
    tresult PLUGIN_API (*getFactoryInfo)(void *, PFactoryInfo *);
    int32 PLUGIN_API (*countClasses)(void *);
    tresult PLUGIN_API (*getClassInfo)(void *, int32, PClassInfo *);
    tresult PLUGIN_API (*createInstance)(void *, FIDString, FIDString, void **);
} IPluginFactoryVTable;

typedef struct SIPluginFactory {
    IPluginFactoryVTable *vtable;
} SIPluginFactory;

#ifdef __cplusplus
}
#endif
