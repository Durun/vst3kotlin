#pragma once

#include "FUnknown.h"
#include "base/ftypes.h"
#include "vst/IComponentHandler.h"

#ifdef __cplusplus
extern "C" {
#endif

typedef struct IComponentHandlerVTable {
    FUnknownVTable FUnknown;
    tresult (*beginEdit)(void *, ParamID);
    tresult (*performEdit)(void *, ParamID, ParamValue);
    tresult (*endEdit)(void *, ParamID);
    tresult (*restartComponent)(void *, int32);
} IComponentHandlerVTable;

typedef struct SIComponentHandler {
    IComponentHandlerVTable *vtable;
} SIComponentHandler;

typedef struct IComponentHandler2VTable {
    FUnknownVTable FUnknown;
    tresult (*setDirty)(void *, TBool);
    tresult (*requestOpenEditor)(void *, FIDString);
    tresult (*startGroupEdit)(void *);
    tresult (*finishGroupEdit)(void *);
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
