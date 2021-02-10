#include "base/IPluginBase.h"

#include <pluginterfaces/base/ipluginbase.h>

Steinberg::IPluginBase* PLUGIN_API cast(IPluginBase* this_ptr) { //private
	return reinterpret_cast<Steinberg::IPluginBase*>(this_ptr);
}

// [IPluginBase] member functions
tresult PLUGIN_API IPluginBase_initialize(IPluginBase* this_ptr, FUnknown* context) {
    return cast(this_ptr)->initialize(
		reinterpret_cast<Steinberg::FUnknown*>(context));
}
tresult PLUGIN_API IPluginBase_terminate(IPluginBase* this_ptr) {
	return cast(this_ptr)->terminate();
}
