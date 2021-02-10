#include "vst/IParameterChanges.h"

#include <pluginterfaces/vst/ivstparameterchanges.h>

Steinberg::Vst::IParamValueQueue* cast(IParamValueQueue* this_ptr) { // private
	return reinterpret_cast<Steinberg::Vst::IParamValueQueue*>(this_ptr);
}
Steinberg::Vst::IParameterChanges* cast(IParameterChanges* this_ptr) { // private
	return reinterpret_cast<Steinberg::Vst::IParameterChanges*>(this_ptr);
}
IParamValueQueue* cast(Steinberg::Vst::IParamValueQueue* this_ptr) { // private
	return reinterpret_cast<IParamValueQueue*>(this_ptr);
}

// [IParamValueQueue] member functions
ParamID PLUGIN_API IParamValueQueue_getParameterId(IParamValueQueue* this_ptr) {
	return cast(this_ptr)->getParameterId();
}
int32 PLUGIN_API IParamValueQueue_getPointCount(IParamValueQueue* this_ptr) {
	return cast(this_ptr)->getPointCount();
}
tresult PLUGIN_API IParamValueQueue_getPoint(IParamValueQueue* this_ptr, int32 index, int32* sampleOffset, ParamValue* value) {
	return cast(this_ptr)->getPoint(index, *sampleOffset, *value);
}
tresult PLUGIN_API IParamValueQueue_addPoint(IParamValueQueue* this_ptr, int32 sampleOffset, ParamValue value, int32* index) {
	return cast(this_ptr)->addPoint(sampleOffset, value, *index);
}

// [IParameterChanges] member functions
int32 PLUGIN_API IParameterChanges_getParameterCount(IParameterChanges* this_ptr) {
	return cast(this_ptr)->getParameterCount();
}
IParamValueQueue* PLUGIN_API IParameterChanges_getParameterData(IParameterChanges* this_ptr, int32 index) {
	auto ret = cast(this_ptr)->getParameterData(index);
	return cast(ret);
}
IParamValueQueue* PLUGIN_API IParameterChanges_addParameterData(IParameterChanges* this_ptr, const ParamID* id, int32* index) {
	auto ret = cast(this_ptr)->addParameterData(*id, *index);
	return cast(ret);
}
