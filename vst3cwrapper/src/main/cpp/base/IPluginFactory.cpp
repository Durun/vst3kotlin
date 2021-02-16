#include "cast.h"


// [IPluginFactory] constructor
IPluginFactory* PLUGIN_API IPluginFactory_new(void* factory) {
	return reinterpret_cast<IPluginFactory*>(factory);
}

// [IPluginFactory] member functions
tresult PLUGIN_API IPluginFactory_queryInterface(IPluginFactory* this_ptr, const TUID _iid, void** obj) {
	return cast(this_ptr)->queryInterface(_iid, obj);
}

uint32 PLUGIN_API IPluginFactory_addRef(IPluginFactory* this_ptr) {
	return cast(this_ptr)->addRef();
}

uint32 PLUGIN_API IPluginFactory_release(IPluginFactory* this_ptr) {
	return cast(this_ptr)->release();
}

tresult PLUGIN_API IPluginFactory_getFactoryInfo(IPluginFactory* this_ptr, PFactoryInfo* info) {
	return cast(this_ptr)->getFactoryInfo(
		reinterpret_cast<Steinberg::PFactoryInfo*>(info)
	);
}

int32 PLUGIN_API IPluginFactory_countClasses(IPluginFactory* this_ptr) {
	return cast(this_ptr)->countClasses();
}

tresult PLUGIN_API IPluginFactory_getClassInfo(IPluginFactory* this_ptr, int32 index, PClassInfo* info) {
	return cast(this_ptr)->getClassInfo(
		index,
		reinterpret_cast<Steinberg::PClassInfo*>(info)
	);
}

tresult PLUGIN_API IPluginFactory_createInstance(IPluginFactory* this_ptr, FUID* cid, FUID* iid, void** obj) {
    return cast(this_ptr)->createInstance(
        *reinterpret_cast<Steinberg::FUID*>(cid),
        *reinterpret_cast<Steinberg::FUID*>(iid), obj);
}

// [IPluginFactory2] member functions
tresult PLUGIN_API getClassInfo2(IPluginFactory2* this_ptr, int32 index, PClassInfo2* info) {
	return cast2(this_ptr)->getClassInfo2(
		index,
		reinterpret_cast<Steinberg::PClassInfo2*>(info)
	);
}

// [IPluginFactory3] member functions
tresult PLUGIN_API getClassInfoUnicode(IPluginFactory3* this_ptr, int32 index, PClassInfoW* info) {
	return cast3(this_ptr)->getClassInfoUnicode(
		index,
		reinterpret_cast<Steinberg::PClassInfoW*>(info)
	);
}

tresult PLUGIN_API setHostContext(IPluginFactory3* this_ptr, FUnknown* context) {
	return cast3(this_ptr)->setHostContext(
		reinterpret_cast<Steinberg::FUnknown*>(context)
	);
}
