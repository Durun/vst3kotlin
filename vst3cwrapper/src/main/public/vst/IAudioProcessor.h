#pragma once

#include "base/FUnknown.h"
#include "vsttypes.h"
#include "IParameterChanges.h"
#include "IEventList.h"

#ifdef __cplusplus
extern "C" {
#endif

enum ComponentFlags {
	kDistributable = 1 << 0,
	kSimpleModeSupported = 1 << 1
};
enum SymbolicSampleSizes {
	kSample32,
	kSample64
};
enum ProcessModes {
	kRealtime,
	kPrefetch,
	kOffline
};
const uint32 kNoTail = 0;
const uint32 kInfiniteTail = kMaxInt32u;
typedef struct ProcessSetup {
	int32 processMode;
	int32 symbolicSampleSize;
	int32 maxSamplesPerBlock;
	SampleRate sampleRate;
} ProcessSetup;
typedef struct AudioBusBuffers {
	int32 numChannels;
	uint64 silenceFlags;
	union {
		Sample32** channelBuffers32;
		Sample64** channelBuffers64;
	};
} AudioBusBuffers;

typedef struct ProcessData {
	int32 processMode;
	int32 symbolicSampleSize;
	int32 numSamples;
	int32 numInputs;
	int32 numOutputs;
	AudioBusBuffers* inputs;
	AudioBusBuffers* outputs;

	IParameterChanges* inputParameterChanges;
	IParameterChanges* outputParameterChanges;
	IEventList* inputEvents;
	IEventList* outputEvents;
	ProcessContext* processContext;
} ProcessData;


/**
 * inherited from [FUnknown]
 */
typedef struct IAudioProcessor {
	VTable *vtable;
} IAudioProcessor;
const TUID IAudioProcessor_iid = INLINE_UID_c(0x42043F99, 0xB7DA453C, 0xA569E79D, 0x9AAEC33D);
// [IAudioProcessor] member functions
tresult PLUGIN_API IAudioProcessor_setBusArrangements(IAudioProcessor* this_ptr, SpeakerArrangement* inputs, int32 numIns, SpeakerArrangement* outputs, int32 numOuts);
tresult PLUGIN_API IAudioProcessor_getBusArrangement(IAudioProcessor* this_ptr, BusDirection dir, int32 index, SpeakerArrangement* arr);
tresult PLUGIN_API IAudioProcessor_canProcessSampleSize(IAudioProcessor* this_ptr, int32 symbolicSampleSize);
uint32 PLUGIN_API IAudioProcessor_getLatencySamples(IAudioProcessor* this_ptr);
tresult PLUGIN_API IAudioProcessor_setupProcessing(IAudioProcessor* this_ptr, ProcessSetup* setup);
tresult PLUGIN_API IAudioProcessor_setProcessing(IAudioProcessor* this_ptr, TBool state);
tresult PLUGIN_API IAudioProcessor_process(IAudioProcessor* this_ptr, ProcessData* data);
uint32 PLUGIN_API IAudioProcessor_getTailSamples(IAudioProcessor* this_ptr);


/**
 * inherited from [FUnknown]
 */
typedef struct IAudioPresentationLatency {
	VTable *vtable;
} IAudioPresentationLatency;
const TUID IAudioPresentationLatency_iid = INLINE_UID_c(0x309ECE78, 0xEB7D4fae, 0x8B2225D9, 0x09FD08B6);
// [IAudioPresentationLatency] member functions
tresult PLUGIN_API IAudioPresentationLatency_setAudioPresentationLatencySamples(IAudioPresentationLatency* this_ptr, BusDirection dir, int32 busIndex, uint32 latencyInSamples);


enum IProcessContextRequirements_Flags {
	kNeedSystemTime = 1 << 0,
	kNeedContinousTimeSamples = 1 << 1,
	kNeedProjectTimeMusic = 1 << 2,
	kNeedBarPositionMusic = 1 << 3,
	kNeedCycleMusic = 1 << 4,
	kNeedSamplesToNextClock = 1 << 5,
	kNeedTempo = 1 << 6,
	kNeedTimeSignature = 1 << 7,
	kNeedChord = 1 << 8,
	kNeedFrameRate = 1 << 9,
	kNeedTransportState = 1 << 10,
};
/**
 * inherited from [FUnknown]
 */
typedef struct IProcessContextRequirements {
	VTable *vtable;
} IProcessContextRequirements;
const TUID IProcessContextRequirements_iid = INLINE_UID_c(0x2A654303, 0xEF764E3D, 0x95B5FE83, 0x730EF6D0);
// [IProcessContextRequirements] member functions
uint32 PLUGIN_API IProcessContextRequirements_getProcessContextRequirements(IProcessContextRequirements* this_ptr);


#ifdef __cplusplus
}
#endif