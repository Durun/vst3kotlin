#include "cast.h"

// [IBStream] member functions
tresult PLUGIN_API IBStream_read(IBStream* this_ptr, void* buffer, int32 numBytes, int32* numBytesRead) {
	return cast(this_ptr)->read(buffer, numBytes, numBytesRead);
}
tresult PLUGIN_API IBStream_write(IBStream* this_ptr, void* buffer, int32 numBytes, int32* numBytesWritten) {
	return cast(this_ptr)->write(buffer, numBytes, numBytesWritten);
}
tresult PLUGIN_API IBStream_seek(IBStream* this_ptr, int64 pos, int32 mode, int64* result) {
	return cast(this_ptr)->seek(pos, mode, result);
}
tresult PLUGIN_API IBStream_tell(IBStream* this_ptr, int64* pos) {
	return cast(this_ptr)->tell(pos);
}

// [ISizeableStream] member functions
tresult PLUGIN_API ISizeableStream_getStreamSize(ISizeableStream* this_ptr, int64* size) {
	return cast(this_ptr)->getStreamSize(*size);
}
tresult PLUGIN_API ISizeableStream_setStreamSize(ISizeableStream* this_ptr, int64 size) {
	return cast(this_ptr)->setStreamSize(size);
}
