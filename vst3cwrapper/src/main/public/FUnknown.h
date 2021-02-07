#pragma once

#include "ftypes.h"
#include "FUID.h"

#include "vtable.h"

#ifdef __cplusplus
extern "C" {
#endif

typedef struct FUnknown {
    VTable *vtable;
} FUnknown;
const TUID FUnknown_iid = INLINE_UID_c(0x00000000, 0x00000000, 0xC0000000, 0x00000046);

// [FUnknown] member functions
tresult FUnknown_queryInterface(FUnknown *this_ptr, const TUID _iid, void **obj);
uint32 FUnknown_addRef(FUnknown *this_ptr);
uint32 FUnknown_release(FUnknown *this_ptr);

#ifdef __cplusplus
}
#endif
