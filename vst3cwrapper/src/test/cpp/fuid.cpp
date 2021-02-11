#include <pluginterfaces/base/fstrdefs.h>
#include <pluginterfaces/base/funknown.h>
#include <stdio.h>

#include <cassert>

int test_fuid() {
	auto fuid = new Steinberg::FUID(0x30313233, 0x34353637, 0x38394041, 0x42434445);  // 0x30 = '0'
	Steinberg::char8 str[64];
	fuid->toString(str);
	printf("FUID: str=%s\n", str);

	auto tuid = new Steinberg::TUID();
	fuid->toTUID(tuid);
	printf("TUID: %s\n", tuid);

	assert(Steinberg::strcmp8(str, "30313233343536373839404142434445") == 0);
	assert(Steinberg::strcmp8(tuid, "0123456789@ABCDE") == 0);
	return 0;
}
