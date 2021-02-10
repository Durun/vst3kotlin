#pragma once

#include "ftypes.h"
#include "FUID.h"
#include "FUnknown.h"
#include "vtable.h"

#ifdef __cplusplus
extern "C" {
#endif

/**
 * inherited from [FUnknown]
 */
typedef struct IPluginBase {
	VTable *vtable;
} IPluginBase;
const TUID IPluginBase_iid = INLINE_UID_c(0x22888DDB, 0x156E45AE, 0x8358B348, 0x08190625);

// [IPluginBase] member functions
tresult PLUGIN_API initialize(IPluginBase* this_ptr, FUnknown* context);
tresult PLUGIN_API terminate(IPluginBase* this_ptr);

#ifdef __cplusplus
}
#endif
