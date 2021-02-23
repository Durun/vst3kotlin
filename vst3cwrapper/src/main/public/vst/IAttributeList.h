#pragma once

#include "base/FUnknown.h"
#include "vsttypes.h"

#ifdef __cplusplus
extern "C" {
#endif

typedef const char* AttrID;

/**
 * inherited from [FUnknown]
 */
typedef struct IAttributeList {
    VTable* vtable;
} IAttributeList;
const TUID IAttributeList_iid = INLINE_UID_c(0x1E5F0AEB, 0xCC7F4533, 0xA2544011, 0x38AD5EE4);
// [IAttributeList] member functions
tresult PLUGIN_API IAttributeList_setInt(IAttributeList* this_ptr, AttrID id, int64 value);
tresult PLUGIN_API IAttributeList_getInt(IAttributeList* this_ptr, AttrID id, int64* value);
tresult PLUGIN_API IAttributeList_setFloat(IAttributeList* this_ptr, AttrID id, double value);
tresult PLUGIN_API IAttributeList_getFloat(IAttributeList* this_ptr, AttrID id, double* value);
tresult PLUGIN_API IAttributeList_setString(IAttributeList* this_ptr, AttrID id, const TChar* string);
tresult PLUGIN_API IAttributeList_getString(IAttributeList* this_ptr, AttrID id, TChar* string, uint32 sizeInBytes);
tresult PLUGIN_API IAttributeList_setBinary(IAttributeList* this_ptr, AttrID id, const void* data, uint32 sizeInBytes);
tresult PLUGIN_API IAttributeList_getBinary(IAttributeList* this_ptr, AttrID id, const void** data, uint32* sizeInBytes);

#ifdef __cplusplus
}
#endif
