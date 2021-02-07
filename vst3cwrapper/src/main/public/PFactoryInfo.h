#pragma once

#include "ftypes.h"

#ifdef __cplusplus
extern "C" {
#endif


enum FactoryFlags {
	kNoFlags = 0,
	kClassesDiscardable = 1 << 0,
	kLicenseCheck = 1 << 1,
	kComponentNonDiscardable = 1 << 3,
	kUnicode = 1 << 4
};

enum {
	PFactoryInfo_kURLSize = 256,
	PFactoryInfo_kEmailSize = 128,
	PFactoryInfo_kNameSize = 64
};

typedef struct PFactoryInfo {
	char8 vendor[PFactoryInfo_kNameSize];
	char8 url[PFactoryInfo_kURLSize];
	char8 email[PFactoryInfo_kEmailSize];
	int32 flags;
} PFactoryInfo;


#ifdef __cplusplus
}
#endif
