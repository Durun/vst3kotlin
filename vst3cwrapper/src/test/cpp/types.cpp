#include <stdio.h>
#include <cassert>

#include "base/ftypes.h"

int test_types() {
	assert(sizeof(int8) == 1);
	assert(sizeof(uint8) == 1);
	assert(sizeof(char8) == 1);
	assert(sizeof(uchar) == 1);

	assert(sizeof(int16) == 2);
	assert(sizeof(uint16) == 2);
	assert(sizeof(char16) == 2);

	assert(sizeof(int32) == 4);
	assert(sizeof(uint32) == 4);

	assert(sizeof(int64) == 8);
	assert(sizeof(uint64) == 8);

	assert(sizeof(bool) == 1);
	assert(sizeof(TBool) == 1);

	assert(sizeof(TSize) == 8);
	assert(sizeof(tresult) == 4);
	assert(sizeof(UCoord) == 4);

	return 0;
}
