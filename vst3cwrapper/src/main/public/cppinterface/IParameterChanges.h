#pragma once

#include "FUnknown.h"
#include "base/ftypes.h"
#include "vst/IParameterChanges.h"

#ifdef __cplusplus
extern "C" {
#endif

#ifndef nullptr
#define nullptr (0)
#endif

uint32 SIParamValueQueue_addRef(void *this_ptr);
uint32 SIParamValueQueue_release(void *this_ptr);
ParamID SIParamValueQueue_getParameterId(IParamValueQueue *this_ptr);
int32 SIParamValueQueue_getPointCount(IParamValueQueue *this_ptr);
tresult SIParamValueQueue_getPoint(
        IParamValueQueue *this_ptr, int32 index, int32 *sampleOffset/*out*/, ParamValue *value/*out*/);
tresult SIParamValueQueue_addPoint(
        IParamValueQueue *this_ptr, int32 sampleOffset, ParamValue value, int32 *index/*out*/);

typedef struct IParamValueQueueVTable {
    FUnknownVTable FUnknown;

    ParamID (*getParameterId)(IParamValueQueue *);

    int32 (*getPointCount)(IParamValueQueue *);

    tresult (*getPoint)(IParamValueQueue *, int32, int32 * /*out*/, ParamValue * /*out*/);

    tresult (*addPoint)(IParamValueQueue *, int32, ParamValue, int32 * /*out*/);
} IParamValueQueueVTable;
const IParamValueQueueVTable IParamValueQueueVTable_def = {
        nullptr,
        SIParamValueQueue_addRef,
        SIParamValueQueue_release,
        SIParamValueQueue_getParameterId,
        SIParamValueQueue_getPointCount,
        SIParamValueQueue_getPoint,
        SIParamValueQueue_addPoint
};
typedef struct SIParamValueQueue {
    const IParamValueQueueVTable *vtable;
    int32 refCount;

    int32 maxPoints;
    int32 _pointCount;
    ParamID _id;
    int32 *_sampleOffset;
    ParamValue *_value;
} SIParamValueQueue;


uint32 SIParameterChanges_addRef(void *this_ptr);
uint32 SIParameterChanges_release(void *this_ptr);
int32 SIParameterChanges_getParameterCount(IParameterChanges *this_ptr);
IParamValueQueue *SIParameterChanges_getParameterData(IParameterChanges *this_ptr, int32 index);
IParamValueQueue *SIParameterChanges_addParameterData(
        IParameterChanges *this_ptr, const ParamID *id, int32 *index/*out*/);

typedef struct IParameterChangesVTable {
    FUnknownVTable FUnknown;

    int32 (*getParameterCount)(IParameterChanges *);

    IParamValueQueue *(*getParameterData)(IParameterChanges *, int32);

    IParamValueQueue *(*addParameterData)(IParameterChanges *, const ParamID *, int32 *  /*out*/);
} IParameterChangesVTable;

const IParameterChangesVTable IParameterChangesVTable_def = {
        nullptr,
        SIParameterChanges_addRef,
        SIParameterChanges_release,
        SIParameterChanges_getParameterCount,
        SIParameterChanges_getParameterData,
        SIParameterChanges_addParameterData
};
typedef struct SIParameterChanges {
    const IParameterChangesVTable *vtable;
    int32 refCount;

    int32 maxParams;
    int32 _paramCount;
    SIParamValueQueue *_params;
} SIParameterChanges;

// managing functions
SIParameterChanges *SIParameterChanges_alloc(int32 maxParams, int32 maxPoints);
void SIParameterChanges_free(SIParameterChanges *ptr);
void SIParamValueQueue_init(SIParamValueQueue *ptr);
void SIParameterChanges_init(SIParameterChanges *ptr);

#ifdef __cplusplus
}
#endif