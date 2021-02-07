#include "IPluginFactory.h"

#include <pluginterfaces/base/ipluginbase.h>

Steinberg::IPluginFactory* cast(IPluginFactory* this_ptr) { // private
	return reinterpret_cast<Steinberg::IPluginFactory*>(this_ptr);
}

tresult IPluginFactory_queryInterface(IPluginFactory* this_ptr, const TUID _iid, void** obj) {
	return cast(this_ptr)->queryInterface(_iid, obj);
}

uint32 IPluginFactory_addRef(IPluginFactory* this_ptr) {
	return cast(this_ptr)->addRef();
}

uint32 IPluginFactory_release(IPluginFactory* this_ptr) {
	return cast(this_ptr)->release();
}

tresult IPluginFactory_getFactoryInfo(IPluginFactory* this_ptr, PFactoryInfo* info) {
	return cast(this_ptr)->getFactoryInfo(
		reinterpret_cast<Steinberg::PFactoryInfo*>(info)
	);
}

int32 IPluginFactory_countClasses(IPluginFactory* this_ptr) {
	return cast(this_ptr)->countClasses();
}

tresult IPluginFactory_getClassInfo(IPluginFactory* this_ptr, int32 index, PClassInfo* info) {
	return cast(this_ptr)->getClassInfo(
		index,
		reinterpret_cast<Steinberg::PClassInfo*>(info)
	);
}

tresult IPluginFactory_createInstance(IPluginFactory* this_ptr, FIDString cid, FIDString _iid, void** obj) {
	return cast(this_ptr)->createInstance(cid, _iid, obj);
}
