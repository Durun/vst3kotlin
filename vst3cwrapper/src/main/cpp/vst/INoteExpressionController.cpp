#include "vst/INoteExpressionController.h"

#include <pluginterfaces/vst/ivstnoteexpression.h>

Steinberg::Vst::INoteExpressionController* cast(INoteExpressionController* this_ptr) { // private
    return reinterpret_cast<Steinberg::Vst::INoteExpressionController*>(this_ptr);
}
Steinberg::Vst::IKeyswitchController* cast(IKeyswitchController* this_ptr) {  // private
    return reinterpret_cast<Steinberg::Vst::IKeyswitchController*>(this_ptr);
}
Steinberg::Vst::NoteExpressionTypeInfo* cast(NoteExpressionTypeInfo* this_ptr) {  // private
    return reinterpret_cast<Steinberg::Vst::NoteExpressionTypeInfo*>(this_ptr);
}
Steinberg::Vst::KeyswitchInfo* cast(KeyswitchInfo* this_ptr) {  // private
    return reinterpret_cast<Steinberg::Vst::KeyswitchInfo*>(this_ptr);
}

// [INoteExpressionController] member functions
int32 PLUGIN_API INoteExpressionController_getNoteExpressionCount(INoteExpressionController* this_ptr, int32 busIndex, int16 channel) {
	return cast(this_ptr)->getNoteExpressionCount(busIndex, channel);
}
tresult PLUGIN_API INoteExpressionController_getNoteExpressionInfo(INoteExpressionController* this_ptr, int32 busIndex, int16 channel, int32 noteExpressionIndex, NoteExpressionTypeInfo* info) {
	return cast(this_ptr)->getNoteExpressionInfo(busIndex, channel, noteExpressionIndex, *cast(info));
}
tresult PLUGIN_API INoteExpressionController_getNoteExpressionStringByValue(INoteExpressionController* this_ptr, int32 busIndex, int16 channel, NoteExpressionTypeID id, NoteExpressionValue valueNormalized, String128 string) {
	return cast(this_ptr)->getNoteExpressionStringByValue(busIndex, channel, id, valueNormalized, string);
}
tresult PLUGIN_API INoteExpressionController_getNoteExpressionValueByString(INoteExpressionController* this_ptr, int32 busIndex, int16 channel, NoteExpressionTypeID id, const TChar* string, NoteExpressionValue* valueNormalized) {
	return cast(this_ptr)->getNoteExpressionValueByString(busIndex, channel, id, string, *valueNormalized)
}

// [IKeyswitchController] member functions
int32 PLUGIN_API IKeyswitchController_getKeyswitchCount(IKeyswitchController* this_ptr, int32 busIndex, int16 channel) {
	return cast(this_ptr)->getKeyswitchCount(busIndex, channel);
}
tresult PLUGIN_API IKeyswitchController_getKeyswitchInfo(IKeyswitchController* this_ptr, int32 busIndex, int16 channel, int32 keySwitchIndex, KeyswitchInfo* info) {
	return cast(this_ptr)->getKeyswitchInfo(busIndex, channel, keySwitchIndex, *cast(info));
}
