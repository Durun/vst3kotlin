
#pragma once

#include "FUnknown.h"
#include "vtable.h"

#ifdef __cplusplus
extern "C" {
#endif


enum IStreamSeekMode {
	kIBSeekSet = 0,
	kIBSeekCur,
	kIBSeekEnd
};


/**
 * inherited from [FUnknown]
 */
typedef struct IBStream {
	VTable* vtable;
} IBStream;
const TUID IBStream_iid = INLINE_UID_c(0xC3BF6EA2, 0x30994752, 0x9B6BF990, 0x1EE33E9B);
// [IBStream] member functions
tresult PLUGIN_API read(IBStream* this_ptr, void* buffer, int32 numBytes, int32* numBytesRead = nullptr);
tresult PLUGIN_API write(IBStream* this_ptr, void* buffer, int32 numBytes, int32* numBytesWritten = nullptr);
tresult PLUGIN_API seek(IBStream* this_ptr, int64 pos, int32 mode, int64* result = nullptr);
tresult PLUGIN_API tell(IBStream* this_ptr, int64* pos);


/**
 * inherited from [FUnknown]
 */
typedef struct ISizeableStream {
	VTable* vtable;
} ISizeableStream;
const TUID ISizeableStream_iid = INLINE_UID_c(0x04F9549E, 0xE02F4E6E, 0x87E86A87, 0x47F4E17F);
// [ISizeableStream] member functions
tresult PLUGIN_API getStreamSize(ISizeableStream* this_ptr, int64& size);
tresult PLUGIN_API setStreamSize(ISizeableStream* this_ptr, int64 size);

#ifdef __cplusplus
}
#endif
