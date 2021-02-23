#include "cast.h"

// [IPluginBase] member functions
tresult PLUGIN_API IComponent_initialize(IComponent* this_ptr, FUnknown* context) {
    return cast(this_ptr)->initialize(
        reinterpret_cast<Steinberg::FUnknown*>(context));
}
tresult PLUGIN_API IComponent_terminate(IComponent* this_ptr) {
    return cast(this_ptr)->terminate();
}

// [IComponent] member functions
tresult PLUGIN_API IComponent_getControllerClassId(IComponent* this_ptr, TUID classId) {
    return cast(this_ptr)->getControllerClassId(classId);
}
tresult PLUGIN_API IComponent_setIoMode(IComponent* this_ptr, IoMode mode) {
    return cast(this_ptr)->setIoMode(mode);
}
int32 PLUGIN_API IComponent_getBusCount(IComponent* this_ptr, MediaType type, BusDirection dir) {
    return cast(this_ptr)->getBusCount(type, dir);
}
tresult PLUGIN_API IComponent_getBusInfo(IComponent* this_ptr, MediaType type, BusDirection dir, int32 index, BusInfo* bus) {
    return cast(this_ptr)->getBusInfo(
        type, dir, index, *reinterpret_cast<Steinberg::Vst::BusInfo*>(bus));
}
tresult PLUGIN_API IComponent_getRoutingInfo(IComponent* this_ptr, RoutingInfo* inInfo, RoutingInfo* outInfo) {
    return cast(this_ptr)->getRoutingInfo(
        *reinterpret_cast<Steinberg::Vst::RoutingInfo*>(inInfo),
        *reinterpret_cast<Steinberg::Vst::RoutingInfo*>(outInfo));
}
tresult PLUGIN_API IComponent_activateBus(IComponent* this_ptr, MediaType type, BusDirection dir, int32 index, TBool state) {
	return cast(this_ptr)->activateBus(type, dir, index, state);
}
tresult PLUGIN_API IComponent_setActive(IComponent* this_ptr, TBool state) {
	return cast(this_ptr)->setActive(state);
}
tresult PLUGIN_API IComponent_setState(IComponent* this_ptr, IBStream* state) {
	return cast(this_ptr)->setState(
		cast(state));
}
tresult PLUGIN_API IComponent_getState(IComponent* this_ptr, IBStream* state) {
	return cast(this_ptr)->getState(
		cast(state));
}
