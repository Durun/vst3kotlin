#include "base/FUID.h"

#include <pluginterfaces/base/fstrdefs.h>
#include <pluginterfaces/base/funknown.h>
#include <stdio.h>

#include <cassert>

int test_fuid() {
    auto id = FUID_new_int(0x30313233, 0x34353637, 0x38394041, 0x42434445);  // 0x30 = '0'
    char8 s[64];
    FUID_toString(id, s);
    printf("FUID: string=%s\n", s);

    TUID t;  // int8*
    FUID_toTUID(id, t);	// t is NOT null-terminated
    auto lastT = t[15];
    t[15] = '\0';
    printf("TUID: data=%s%c\n", t, lastT);

    assert(strcmp(s, "30313233343536373839404142434445") == 0);
    assert(strcmp(t, "0123456789@ABCD") == 0);
    assert(lastT == 'E');

    return 0;
}
