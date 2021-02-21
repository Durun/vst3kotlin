#pragma once

#include "base/FUnknown.h"
#include "vst/IMessage.h"
#include "vsttypes.h"

#ifdef __cplusplus
extern "C" {
#endif

/**
 * inherited from [FUnknown]
 */
typedef struct IConnectionPoint {
    VTable* vtable;
} IConnectionPoint;
const TUID IConnectionPoint_iid = INLINE_UID_c(0x70A4156F, 0x6E6E4026, 0x989148BF, 0xAA60D8D1);
// [IConnectionPoint] member functions
tresult PLUGIN_API IConnectionPoint_connect(IConnectionPoint* this_ptr, IConnectionPoint* other);
tresult PLUGIN_API IConnectionPoint_disconnect(IConnectionPoint* this_ptr, IConnectionPoint* other);
tresult PLUGIN_API IConnectionPoint_notify(IConnectionPoint* this_ptr, IMessage* message);

#ifdef __cplusplus
}
#endif
