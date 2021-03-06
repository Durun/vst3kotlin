#include "cast.h"

Steinberg::FUID* cast(FUID* this_ptr) {
	return reinterpret_cast<Steinberg::FUID*>(this_ptr);
}
const Steinberg::FUID* cast(const FUID* this_ptr) {
	return reinterpret_cast<const Steinberg::FUID*>(this_ptr);
}
FUID* cast(Steinberg::FUID* this_ptr) {
	return reinterpret_cast<FUID*>(this_ptr);
}
Steinberg::FUnknown* cast(FUnknown* this_ptr) {
	return reinterpret_cast<Steinberg::FUnknown*>(this_ptr);
}
Steinberg::IPluginFactory* PLUGIN_API cast(IPluginFactory* this_ptr) {
	return reinterpret_cast<Steinberg::IPluginFactory*>(this_ptr);
}
Steinberg::IPluginFactory2* PLUGIN_API cast2(IPluginFactory2* this_ptr) {
	return reinterpret_cast<Steinberg::IPluginFactory2*>(this_ptr);
}
Steinberg::IPluginFactory3* PLUGIN_API cast3(IPluginFactory3* this_ptr) {
	return reinterpret_cast<Steinberg::IPluginFactory3*>(this_ptr);
}
Steinberg::IBStream* cast(IBStream* this_ptr) {
	return reinterpret_cast<Steinberg::IBStream*>(this_ptr);
}
Steinberg::IPluginBase* PLUGIN_API cast(IPluginBase* this_ptr) {  //private
	return reinterpret_cast<Steinberg::IPluginBase*>(this_ptr);
}
Steinberg::ISizeableStream* cast(ISizeableStream* this_ptr) {
	return reinterpret_cast<Steinberg::ISizeableStream*>(this_ptr);
}
Steinberg::IPlugView* cast(IPlugView* this_ptr) {
	return reinterpret_cast<Steinberg::IPlugView*>(this_ptr);
}
IPlugView* cast(Steinberg::IPlugView* this_ptr) {
    return reinterpret_cast<IPlugView*>(this_ptr);
}
Steinberg::Vst::IAttributeList* cast(IAttributeList* this_ptr) {
	return reinterpret_cast<Steinberg::Vst::IAttributeList*>(this_ptr);
}
IAttributeList* cast(Steinberg::Vst::IAttributeList* this_ptr) {
    return reinterpret_cast<IAttributeList*>(this_ptr);
}
Steinberg::IPlugFrame* cast(IPlugFrame* this_ptr) {
    return reinterpret_cast<Steinberg::IPlugFrame*>(this_ptr);
}
Steinberg::ViewRect* cast(ViewRect* this_ptr) {
    return reinterpret_cast<Steinberg::ViewRect*>(this_ptr);
}
Steinberg::Vst::IAudioProcessor* cast(IAudioProcessor* this_ptr) {
	return reinterpret_cast<Steinberg::Vst::IAudioProcessor*>(this_ptr);
}
Steinberg::Vst::IAudioPresentationLatency* cast(IAudioPresentationLatency* this_ptr) {
	return reinterpret_cast<Steinberg::Vst::IAudioPresentationLatency*>(this_ptr);
}
Steinberg::Vst::IProcessContextRequirements* cast(IProcessContextRequirements* this_ptr) {
	return reinterpret_cast<Steinberg::Vst::IProcessContextRequirements*>(this_ptr);
}
Steinberg::Vst::ProcessSetup* cast(ProcessSetup* this_ptr) {
	return reinterpret_cast<Steinberg::Vst::ProcessSetup*>(this_ptr);
}
Steinberg::Vst::ProcessData* cast(ProcessData* this_ptr) {
	return reinterpret_cast<Steinberg::Vst::ProcessData*>(this_ptr);
}
Steinberg::Vst::IComponent* cast(IComponent* this_ptr) {
	return reinterpret_cast<Steinberg::Vst::IComponent*>(this_ptr);
}
Steinberg::Vst::IComponentHandlerBusActivation* cast(IComponentHandlerBusActivation* this_ptr) {
    return reinterpret_cast<Steinberg::Vst::IComponentHandlerBusActivation*>(this_ptr);
}
Steinberg::Vst::IConnectionPoint* cast(IConnectionPoint* this_ptr) {
    return reinterpret_cast<Steinberg::Vst::IConnectionPoint*>(this_ptr);
}
Steinberg::Vst::IEditController* cast(IEditController* this_ptr) {
    return reinterpret_cast<Steinberg::Vst::IEditController*>(this_ptr);
}
Steinberg::Vst::IEditController2* cast(IEditController2* this_ptr) {
    return reinterpret_cast<Steinberg::Vst::IEditController2*>(this_ptr);
}
Steinberg::Vst::ParameterInfo* cast(ParameterInfo* this_ptr) {
    return reinterpret_cast<Steinberg::Vst::ParameterInfo*>(this_ptr);
}
Steinberg::Vst::ParamValue* cast(ParamValue* this_ptr) {
    return reinterpret_cast<Steinberg::Vst::ParamValue*>(this_ptr);
}
Steinberg::Vst::IComponentHandler* cast(IComponentHandler* this_ptr) {
    return reinterpret_cast<Steinberg::Vst::IComponentHandler*>(this_ptr);
}
Steinberg::Vst::IComponentHandler2* cast(IComponentHandler2* this_ptr) {
    return reinterpret_cast<Steinberg::Vst::IComponentHandler2*>(this_ptr);
}
Steinberg::Vst::IEventList* cast(IEventList* this_ptr) {
	return reinterpret_cast<Steinberg::Vst::IEventList*>(this_ptr);
}
Steinberg::Vst::Event* cast(Event* this_ptr) {
	return reinterpret_cast<Steinberg::Vst::Event*>(this_ptr);
}
Steinberg::Vst::IProgress* cast(IProgress* this_ptr) {
    return reinterpret_cast<Steinberg::Vst::IProgress*>(this_ptr);
}
Steinberg::Vst::IProgress::ProgressType* cast(ProgressType* this_ptr) {
	return reinterpret_cast<Steinberg::Vst::IProgress::ProgressType*>(this_ptr);
}
Steinberg::Vst::IMessage* cast(IMessage* this_ptr) {
    return reinterpret_cast<Steinberg::Vst::IMessage*>(this_ptr);
}
Steinberg::Vst::IMidiMapping* cast(IMidiMapping* this_ptr) {
    return reinterpret_cast<Steinberg::Vst::IMidiMapping*>(this_ptr);
}
Steinberg::Vst::INoteExpressionController* cast(INoteExpressionController* this_ptr) {
	return reinterpret_cast<Steinberg::Vst::INoteExpressionController*>(this_ptr);
}
Steinberg::Vst::IKeyswitchController* cast(IKeyswitchController* this_ptr) {
	return reinterpret_cast<Steinberg::Vst::IKeyswitchController*>(this_ptr);
}
Steinberg::Vst::NoteExpressionTypeInfo* cast(NoteExpressionTypeInfo* this_ptr) {
	return reinterpret_cast<Steinberg::Vst::NoteExpressionTypeInfo*>(this_ptr);
}
Steinberg::Vst::KeyswitchInfo* cast(KeyswitchInfo* this_ptr) {
	return reinterpret_cast<Steinberg::Vst::KeyswitchInfo*>(this_ptr);
}
Steinberg::Vst::IParamValueQueue* cast(IParamValueQueue* this_ptr) {
	return reinterpret_cast<Steinberg::Vst::IParamValueQueue*>(this_ptr);
}
Steinberg::Vst::IParameterChanges* cast(IParameterChanges* this_ptr) {
	return reinterpret_cast<Steinberg::Vst::IParameterChanges*>(this_ptr);
}
IParamValueQueue* cast(Steinberg::Vst::IParamValueQueue* this_ptr) {
	return reinterpret_cast<IParamValueQueue*>(this_ptr);
}
