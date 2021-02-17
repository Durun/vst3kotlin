#pragma once

#include "base/FUnknown.h"
#include "vsttypes.h"

#ifdef __cplusplus
extern "C" {
#endif

/**
 * inherited from [FUnknown]
 */
typedef struct IMidiMapping {
    VTable* vtable;
} IMidiMapping;
const TUID IMidiMapping_iid = INLINE_UID_c(0xDF0FF9F7, 0x49B74669, 0xB63AB732, 0x7ADBF5E5);
// [IMidiMapping] member functions
tresult PLUGIN_API getMidiControllerAssignment(IMidiMapping* this_ptr, int32 busIndex, int16 channel, CtrlNumber midiControllerNumber, ParamID* id /*out*/);

#ifdef __cplusplus
}
#endif
