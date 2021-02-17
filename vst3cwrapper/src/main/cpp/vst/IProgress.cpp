#pragma once

#include "cast.h"

// [IProgress] member functions
tresult PLUGIN_API start(IProgress* this_ptr, ProgressType type, const tchar* optionalDescription, uint64* outID) {
    return cast(this_ptr)->start(*cast(&type), optionalDescription, *outID);
}
tresult PLUGIN_API update(IProgress* this_ptr, uint64 id, ParamValue normValue) {
    return cast(this_ptr)->update(id, normValue);
}
tresult PLUGIN_API finish(IProgress* this_ptr, uint64 id) {
    return cast(this_ptr)->finish(id);
}
