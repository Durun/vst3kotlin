#pragma once

#include "ftypes.h"
#include "FUID.h"

#ifdef __cplusplus
extern "C" {
#endif


enum ClassCardinality {
	kManyInstances = 0x7FFFFFFF
};

enum {
	PClassInfo_kCategorySize = 32,
	PClassInfo_kNameSize = 64
};

typedef struct PClassInfo {
	TUID cid;
	int32 cardinality;
	char8 category[PClassInfo_kCategorySize];
	char8 name[PClassInfo_kNameSize];
} PClassInfo;


#ifdef __cplusplus
}
#endif
