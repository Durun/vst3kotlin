#include <stdio.h>
#include "loadFactory.h"
#include "getFunctionPointer.h"

Steinberg::IPluginFactory* loadFactory(void* handle) {
    auto factoryProc = getFunctionPointer<GetFactoryProc>(handle, "GetPluginFactory");
    if (!factoryProc) fprintf(stderr, "The shared library does not export the required 'GetPluginFactory' function");

	auto moduleEntry = getFunctionPointer<ModuleEntryFunc>(handle, "ModuleEntry");
    if (!moduleEntry) fprintf(stderr, "The shared library does not export the required 'ModuleEntry' function");
    if (!moduleEntry(handle)) fprintf(stderr, "Calling 'ModuleEntry' failed");

	auto factory = factoryProc();
    if (!factory) fprintf(stderr, "Calling 'GetPluginFactory' returned nullptr");
	return factory;
}
