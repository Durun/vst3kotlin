#pragma once

#include "ftypes.h"
#include "vtable.h"

#ifdef __cplusplus
extern "C" {
#endif

typedef int8 TUID[16];

void TUID_from4Int(TUID out, uint32 i1, uint32 i2, uint32 i3, uint32 i4);

typedef struct FUID {
	VTable *vtable;
	TUID tuid;
} FUID;
void FUID_from4Int(FUID *out, uint32 i1, uint32 i2, uint32 i3, uint32 i4);
void FUID_fromTUID(FUID *out, TUID tuid);


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
