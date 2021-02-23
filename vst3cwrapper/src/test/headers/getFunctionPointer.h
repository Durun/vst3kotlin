#pragma once
#include <dlfcn.h>
#include <pluginterfaces/base/funknown.h>

template <typename T>
T getFunctionPointer(void* module, const char* name) {
    return reinterpret_cast<T>(dlsym(module, name));
}

extern "C" {
using ModuleEntryFunc = bool(PLUGIN_API*)(void*);
using ModuleExitFunc = bool(PLUGIN_API*)();
}
