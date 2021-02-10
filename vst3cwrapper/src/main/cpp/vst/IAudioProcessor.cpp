#include "vst/IAudioProcessor.h"

#include <pluginterfaces/vst/ivstaudioprocessor.h>

Steinberg::Vst::IAudioProcessor* cast(IAudioProcessor* this_ptr) {  // private
    return reinterpret_cast<Steinberg::Vst::IAudioProcessor*>(this_ptr);
}
Steinberg::Vst::IAudioPresentationLatency* cast(IAudioPresentationLatency* this_ptr) {  // private
    return reinterpret_cast<Steinberg::Vst::IAudioPresentationLatency*>(this_ptr);
}
Steinberg::Vst::IProcessContextRequirements* cast(IProcessContextRequirements* this_ptr) {  // private
    return reinterpret_cast<Steinberg::Vst::IProcessContextRequirements*>(this_ptr);
}
Steinberg::Vst::ProcessSetup* cast(ProcessSetup* this_ptr) {  // private
    return reinterpret_cast<Steinberg::Vst::ProcessSetup*>(this_ptr);
}
Steinberg::Vst::ProcessData* cast(ProcessData* this_ptr) {  // private
    return reinterpret_cast<Steinberg::Vst::ProcessData*>(this_ptr);
}

// [IAudioProcessor] member functions
tresult PLUGIN_API IAudioProcessor_setBusArrangements(IAudioProcessor* this_ptr, SpeakerArrangement* inputs, int32 numIns, SpeakerArrangement* outputs, int32 numOuts) {
	return cast(this_ptr)->setBusArrangements(inputs, numIns, outputs, numOuts);
}
tresult PLUGIN_API IAudioProcessor_getBusArrangement(IAudioProcessor* this_ptr, BusDirection dir, int32 index, SpeakerArrangement* arr) {
	return cast(this_ptr)->getBusArrangement(dir, index, *arr);
}
tresult PLUGIN_API IAudioProcessor_canProcessSampleSize(IAudioProcessor* this_ptr, int32 symbolicSampleSize) {
	return cast(this_ptr)->canProcessSampleSize(symbolicSampleSize);
}
uint32 PLUGIN_API IAudioProcessor_getLatencySamples(IAudioProcessor* this_ptr) {
	return cast(this_ptr)->getLatencySamples();
}
tresult PLUGIN_API IAudioProcessor_setupProcessing(IAudioProcessor* this_ptr, ProcessSetup* setup) {
	return cast(this_ptr)->setupProcessing(*cast(setup));
}
tresult PLUGIN_API IAudioProcessor_setProcessing(IAudioProcessor* this_ptr, TBool state) {
	return cast(this_ptr)->setProcessing(state);
}
tresult PLUGIN_API IAudioProcessor_process(IAudioProcessor* this_ptr, ProcessData* data) {
	return cast(this_ptr)->process(*cast(data));
}
uint32 PLUGIN_API IAudioProcessor_getTailSamples(IAudioProcessor* this_ptr) {
	return cast(this_ptr)->getTailSamples();
}

// [IAudioPresentationLatency] member functions
tresult PLUGIN_API IAudioPresentationLatency_setAudioPresentationLatencySamples(IAudioPresentationLatency* this_ptr, BusDirection dir, int32 busIndex, uint32 latencyInSamples) {
	return cast(this_ptr)->setAudioPresentationLatencySamples(dir, busIndex, latencyInSamples);
}

// [IProcessContextRequirements] member functions
uint32 PLUGIN_API IProcessContextRequirements_getProcessContextRequirements(IProcessContextRequirements* this_ptr) {
	return cast(this_ptr)->getProcessContextRequirements();
}
