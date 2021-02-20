#pragma once

#include "FUnknown.h"
#include "base/ftypes.h"
#include "vst/IComponentHandler.h"

#ifdef __cplusplus
extern "C" {
#endif

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

// functions
SIComponentHandler *SIComponentHandler_alloc();
SIComponentHandler2 *SIComponentHandler2_alloc();
void SIComponentHandler_free(SIComponentHandler *ptr);
void SIComponentHandler2_free(SIComponentHandler2 *ptr);

#ifdef __cplusplus
}
#endif
