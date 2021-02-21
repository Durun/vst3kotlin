#pragma once

#include "base/FUnknown.h"
#include "vst/IAttributeList.h"
#include "vsttypes.h"

#ifdef __cplusplus
extern "C" {
#endif

/**
 * inherited from [FUnknown]
 */
typedef struct IMessage {
    VTable* vtable;
} IMessage;
const TUID IMessage_iid = INLINE_UID_c(0x936F033B, 0xC6C047DB, 0xBB0882F8, 0x13C1E613);
// [IMessage] member functions
FIDString PLUGIN_API IMessage_getMessageID(IMessage* this_ptr);
void PLUGIN_API IMessage_setMessageID(IMessage* this_ptr, FIDString id /*in*/);
IAttributeList* PLUGIN_API IMessage_getAttributes(IMessage* this_ptr);

#ifdef __cplusplus
}
#endif
