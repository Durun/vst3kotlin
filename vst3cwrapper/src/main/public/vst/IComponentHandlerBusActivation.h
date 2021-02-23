#pragma once

#include "base/FUnknown.h"
#include "vsttypes.h"

#ifdef __cplusplus
extern "C" {
#endif

/**
 * inherited from [FUnknown]
 */
typedef struct IComponentHandlerBusActivation {
    VTable *vtable;
} IComponentHandlerBusActivation;
const TUID IComponentHandlerBusActivation_iid = INLINE_UID_c(0x067D02C1, 0x5B4E274D, 0xA92D90FD, 0x6EAF7240);
// [IComponentHandlerBusActivation] member functions
tresult PLUGIN_API IComponentHandlerBusActivation_requestBusActivation(IComponentHandlerBusActivation* this_ptr, MediaType type, BusDirection dir, int32 index, TBool state);

#ifdef __cplusplus
}
#endif
