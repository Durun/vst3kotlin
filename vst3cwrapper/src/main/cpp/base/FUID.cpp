#include "cast.h"

void TUID_from4Int(TUID out, uint32 i1, uint32 i2, uint32 i3, uint32 i4) {
	TUID newId = INLINE_UID_c(i1, i2, i3, i4);
	for (int i = 0; i < 16; i++) {
		out[i] = newId[i];
	}
}

void FUID_from4Int(FUID *out, uint32 i1, uint32 i2, uint32 i3, uint32 i4) {
    TUID newId = INLINE_UID_c(i1, i2, i3, i4);
    for (int i = 0; i < 16; i++) {
        out->tuid[i] = newId[i];
    }
}

void FUID_fromTUID(FUID *out, TUID tuid) {
    for (int i = 0; i < 16; i++) {
        out->tuid[i] = tuid[i];
    }
}
