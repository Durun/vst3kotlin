#pragma once

#include "ftypes.h"
#include "vtable.h"

#ifdef __cplusplus
extern "C" {
#endif

typedef int8 TUID[16];

typedef struct FUID {
	VTable *vtable;
	TUID tuid;
} FUID;
// [FUID] constructors
FUID *FUID_new_int(uint32 i1, uint32 i2, uint32 i3, uint32 i4);
// [FUID] member functions
bool FUID_generate (FUID *this_ptr);
bool FUID_isValid(FUID *this_ptr);
FUID *FUID_set(FUID *this_ptr, const FUID *f);
bool FUID_equals(FUID *this_ptr, const FUID *f);
bool FUID_lessThan(FUID *this_ptr, const FUID *f);
uint32 FUID_getLong1(FUID *this_ptr);
uint32 FUID_getLong2(FUID *this_ptr);
uint32 FUID_getLong3(FUID *this_ptr);
uint32 FUID_getLong4(FUID *this_ptr);
void FUID_from4Int(FUID *this_ptr, uint32 d1, uint32 d2, uint32 d3, uint32 d4);
void FUID_to4Int(FUID *this_ptr, uint32 *d1, uint32 *d2, uint32 *d3, uint32 *d4);
void FUID_toString(FUID *this_ptr, char8 *string);
bool FUID_fromString(FUID *this_ptr, const char8 *string);
void FUID_toRegistryString(FUID *this_ptr, char8 *string);
bool FUID_fromRegistryString(FUID *this_ptr, const char8 *string);
void *FUID_toTUID(FUID *this_ptr, TUID result);
void FUID_print(FUID *this_ptr, char8 *string, int32 style);
enum UIDPrintStyle {
    UIDPrintStyle_kINLINE_UID,
    UIDPrintStyle_kDECLARE_UID,
    UIDPrintStyle_kFUID,
    UIDPrintStyle_kCLASS_UID
};


#if COM_COMPATIBLE
#define INLINE_UID_c(l1, l2, l3, l4)                                                          \
	{                                                                                         \
		(int8)(((uint32)(l1)&0x000000FF)), (int8)(((uint32)(l1)&0x0000FF00) >> 8),            \
			(int8)(((uint32)(l1)&0x00FF0000) >> 16), (int8)(((uint32)(l1)&0xFF000000) >> 24), \
			(int8)(((uint32)(l2)&0x00FF0000) >> 16), (int8)(((uint32)(l2)&0xFF000000) >> 24), \
			(int8)(((uint32)(l2)&0x000000FF)), (int8)(((uint32)(l2)&0x0000FF00) >> 8),        \
			(int8)(((uint32)(l3)&0xFF000000) >> 24), (int8)(((uint32)(l3)&0x00FF0000) >> 16), \
			(int8)(((uint32)(l3)&0x0000FF00) >> 8), (int8)(((uint32)(l3)&0x000000FF)),        \
			(int8)(((uint32)(l4)&0xFF000000) >> 24), (int8)(((uint32)(l4)&0x00FF0000) >> 16), \
			(int8)(((uint32)(l4)&0x0000FF00) >> 8), (int8)(((uint32)(l4)&0x000000FF))         \
	}
#else
#define INLINE_UID_c(l1, l2, l3, l4)                                                          \
	{                                                                                         \
		(int8)(((uint32)(l1)&0xFF000000) >> 24), (int8)(((uint32)(l1)&0x00FF0000) >> 16),     \
			(int8)(((uint32)(l1)&0x0000FF00) >> 8), (int8)(((uint32)(l1)&0x000000FF)),        \
			(int8)(((uint32)(l2)&0xFF000000) >> 24), (int8)(((uint32)(l2)&0x00FF0000) >> 16), \
			(int8)(((uint32)(l2)&0x0000FF00) >> 8), (int8)(((uint32)(l2)&0x000000FF)),        \
			(int8)(((uint32)(l3)&0xFF000000) >> 24), (int8)(((uint32)(l3)&0x00FF0000) >> 16), \
			(int8)(((uint32)(l3)&0x0000FF00) >> 8), (int8)(((uint32)(l3)&0x000000FF)),        \
			(int8)(((uint32)(l4)&0xFF000000) >> 24), (int8)(((uint32)(l4)&0x00FF0000) >> 16), \
			(int8)(((uint32)(l4)&0x0000FF00) >> 8), (int8)(((uint32)(l4)&0x000000FF))         \
	}
#endif

#ifdef __cplusplus
}
#endif
