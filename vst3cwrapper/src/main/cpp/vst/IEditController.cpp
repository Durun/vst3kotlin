#pragma once

#include "cast.h"

// [IEditController] member functions
tresult PLUGIN_API setComponentState (IEditController* this_ptr, IBStream* state) {
    return cast(this_ptr)->setComponentState(cast(state));
}
tresult PLUGIN_API setState (IEditController* this_ptr, IBStream* state) {
    return cast(this_ptr)->setState(cast(state));
}
tresult PLUGIN_API getState(IEditController* this_ptr, IBStream* state) {
    return cast(this_ptr)->getState(cast(state));
}
int32 PLUGIN_API getParameterCount(IEditController* this_ptr) {
    return cast(this_ptr)->getParameterCount();
}
tresult PLUGIN_API getParameterInfo (IEditController* this_ptr, int32 paramIndex, ParameterInfo* info /*out*/) {
    return cast(this_ptr)->getParameterInfo(paramIndex, *cast(info));
}
tresult PLUGIN_API getParamStringByValue (IEditController* this_ptr, ParamID id, ParamValue valueNormalized /*in*/, String128 string /*out*/) {
    return cast(this_ptr)->getParamStringByValue(id, valueNormalized, string);
}
tresult PLUGIN_API getParamValueByString (IEditController* this_ptr, ParamID id, TChar* string /*in*/, ParamValue* valueNormalized /*out*/) {
    return cast(this_ptr)->getParamValueByString(id, string, *cast(valueNormalized));
}
ParamValue PLUGIN_API normalizedParamToPlain (IEditController* this_ptr, ParamID id, ParamValue valueNormalized) {
    return cast(this_ptr)->normalizedParamToPlain(id, valueNormalized);
}
ParamValue PLUGIN_API plainParamToNormalized(IEditController* this_ptr, ParamID id, ParamValue plainValue) {
    return cast(this_ptr)->plainParamToNormalized(id, plainValue);
}
ParamValue PLUGIN_API getParamNormalized (IEditController* this_ptr, ParamID id) {
    return cast(this_ptr)->getParamNormalized(id);
}
tresult PLUGIN_API setParamNormalized (IEditController* this_ptr, ParamID id, ParamValue value) {
    return cast(this_ptr)->setParamNormalized(id, value);
}
tresult PLUGIN_API setComponentHandler(IEditController* this_ptr, IComponentHandler* handler) {
    return cast(this_ptr)->setComponentHandler(cast(handler));
}
IPlugView* PLUGIN_API createView(IEditController* this_ptr, FIDString name) {
    auto ret = cast(this_ptr)->createView(name);
    return cast(ret);
}

// [IEditController2] member functions
tresult PLUGIN_API setKnobMode(IEditController2* this_ptr, KnobMode mode) {
    return cast(this_ptr)->setKnobMode(mode);
}
tresult PLUGIN_API openHelp(IEditController2* this_ptr, TBool onlyCheck) {
    return cast(this_ptr)->openHelp(onlyCheck);
}
tresult PLUGIN_API openAboutBox(IEditController2* this_ptr, TBool onlyCheck) {
    return cast(this_ptr)->openAboutBox(onlyCheck);
}
