#pragma once

#include "base/FUnknown.h"
#include "vsttypes.h"

#ifdef __cplusplus
extern "C" {
#endif

typedef enum ProgressType {
    AsyncStateRestoration = 0,  ///< plug-in state is restored async (in a background Thread)
    UIBackgroundTask            ///< a plug-in task triggered by a UI action
} ProgressType;

/**
 * inherited from [FUnknown]
 */
typedef struct IProgress {
	VTable *vtable;
} IProgress;
const TUID IProgress_iid = INLINE_UID_c(0x00C9DC5B, 0x9D904254, 0x91A388C8, 0xB4E91B69);
// [IProgress] member functions
tresult PLUGIN_API IProgress_start(IProgress* this_ptr, ProgressType type, const tchar* optionalDescription, uint64* outID);
tresult PLUGIN_API IProgress_update(IProgress* this_ptr, uint64 id, ParamValue normValue);
tresult PLUGIN_API IProgress_finish(IProgress* this_ptr, uint64 id);

#ifdef __cplusplus
}
#endif
