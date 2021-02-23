#pragma once

#include "base/FUnknown.h"
#include "base/FUID.h"
#include "vsttypes.h"
#include "vtable.h"

#ifdef __cplusplus
extern "C" {
#endif

/**
 * inherited from [FUnknown]
 */
typedef struct IParamValueQueue {
	VTable *vtable;
} IParamValueQueue;
const TUID IParamValueQueue_iid = INLINE_UID_c(0x01263A18, 0xED074F6F, 0x98C9D356, 0x4686F9BA);
// [IParamValueQueue] member functions
ParamID PLUGIN_API IParamValueQueue_getParameterId(IParamValueQueue* this_ptr);
int32 PLUGIN_API IParamValueQueue_getPointCount(IParamValueQueue* this_ptr);
tresult PLUGIN_API IParamValueQueue_getPoint(IParamValueQueue* this_ptr, int32 index, int32* sampleOffset, ParamValue* value);
tresult PLUGIN_API IParamValueQueue_addPoint(IParamValueQueue* this_ptr, int32 sampleOffset, ParamValue value, int32* index);

/**
 * inherited from [IPluginBase]
 */
typedef struct IParameterChanges {
    VTable* vtable;
} IParameterChanges;
const TUID IParameterChanges_iid = INLINE_UID_c(0xA4779663, 0x0BB64A56, 0xB44384A8, 0x466FEB9D);
// [IParameterChanges] member functions
int32 PLUGIN_API IParameterChanges_getParameterCount(IParameterChanges* this_ptr);
IParamValueQueue* PLUGIN_API IParameterChanges_getParameterData(IParameterChanges* this_ptr, int32 index);
IParamValueQueue* PLUGIN_API IParameterChanges_addParameterData(IParameterChanges* this_ptr, const ParamID* id, int32* index);


#ifdef __cplusplus
}
#endif
