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

// version 1
typedef struct PClassInfo {
	TUID cid;
	int32 cardinality;
	char8 category[PClassInfo_kCategorySize];
	char8 name[PClassInfo_kNameSize];
} PClassInfo;

// version 2
enum {
	kVendorSize = 64,
	kVersionSize = 64,
	kSubCategoriesSize = 128
};
typedef struct PClassInfo2 {
	TUID cid;
	int32 cardinality;
	char8 category[PClassInfo_kCategorySize];
	char8 name[PClassInfo_kNameSize];
	// version 2
	uint32 classFlags;
	char8 subCategories[kSubCategoriesSize];
	char8 vendor[kVendorSize];
	char8 version[kVersionSize];
	char8 sdkVersion[kVersionSize];
} PClassInfo2;

// Unicode version
typedef struct PClassInfoW {
	TUID cid;
	int32 cardinality;
	char8 category[PClassInfo_kCategorySize];
	char16 name[PClassInfo_kNameSize];
	// version 2
	uint32 classFlags;
	char8 subCategories[kSubCategoriesSize];
	char16 vendor[kVendorSize];
	char16 version[kVersionSize];
	char16 sdkVersion[kVersionSize];
} PClassInfoW;

#ifdef __cplusplus
}
#endif
