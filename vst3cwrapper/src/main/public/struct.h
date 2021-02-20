#pragma once

#include "base/FUID.h"
#include "base/PClassInfo.h"
#include "base/PFactoryInfo.h"
#include "base/ftypes.h"
#include "vst/IComponentHandler.h"

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

typedef struct IComponentHandlerVTable {
    FUnknownVTable FUnknown;
    tresult PLUGIN_API (*beginEdit)(void *, ParamID);
    tresult PLUGIN_API (*performEdit)(void *, ParamID, ParamValue);
    tresult PLUGIN_API (*endEdit)(void *, ParamID);
    tresult PLUGIN_API (*restartComponent)(void *, int32);
} IComponentHandlerVTable;

typedef struct SIComponentHandler {
    IComponentHandlerVTable *vtable;
} SIComponentHandler;

SIComponentHandler *SIComponentHandler_alloc();
void SIComponentHandler_free(SIComponentHandler *ptr);

typedef struct IComponentHandler2VTable {
    FUnknownVTable FUnknown;
    tresult PLUGIN_API (*setDirty)(void *, TBool);
    tresult PLUGIN_API (*requestOpenEditor)(void *, FIDString);
    tresult PLUGIN_API (*startGroupEdit)(void *);
    tresult PLUGIN_API (*finishGroupEdit)(void *);
} IComponentHandler2VTable;

typedef struct SIComponentHandler2 {
    IComponentHandler2VTable *vtable;
} SIComponentHandler2;

SIComponentHandler2 *SIComponentHandler2_alloc();
void SIComponentHandler2_free(SIComponentHandler2 *ptr);

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
