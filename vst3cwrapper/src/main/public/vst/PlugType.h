#pragma once

#include "vsttypes.h"

#ifdef __cplusplus
extern "C" {
#endif

const VST_CString PlugType_kFxAnalyzer			= "Fx|Analyzer";
const VST_CString PlugType_kFxDelay				= "Fx|Delay";
const VST_CString PlugType_kFxDistortion		= "Fx|Distortion";
const VST_CString PlugType_kFxDynamics			= "Fx|Dynamics";
const VST_CString PlugType_kFxEQ				= "Fx|EQ";
const VST_CString PlugType_kFxFilter			= "Fx|Filter";
const VST_CString PlugType_kFx					= "Fx";
const VST_CString PlugType_kFxInstrument		= "Fx|Instrument";
const VST_CString PlugType_kFxInstrumentExternal= "Fx|Instrument|External";
const VST_CString PlugType_kFxSpatial			= "Fx|Spatial";
const VST_CString PlugType_kFxGenerator			= "Fx|Generator";
const VST_CString PlugType_kFxMastering			= "Fx|Mastering";
const VST_CString PlugType_kFxModulation		= "Fx|Modulation";
const VST_CString PlugType_kFxPitchShift		= "Fx|Pitch Shift";
const VST_CString PlugType_kFxRestoration		= "Fx|Restoration";
const VST_CString PlugType_kFxReverb			= "Fx|Reverb";
const VST_CString PlugType_kFxSurround			= "Fx|Surround";
const VST_CString PlugType_kFxTools				= "Fx|Tools";
const VST_CString PlugType_kFxNetwork			= "Fx|Network";

const VST_CString PlugType_kInstrument			= "Instrument";
const VST_CString PlugType_kInstrumentDrum		= "Instrument|Drum";
const VST_CString PlugType_kInstrumentExternal	= "Instrument|External";
const VST_CString PlugType_kInstrumentPiano		= "Instrument|Piano";
const VST_CString PlugType_kInstrumentSampler	= "Instrument|Sampler";
const VST_CString PlugType_kInstrumentSynth		= "Instrument|Synth";
const VST_CString PlugType_kInstrumentSynthSampler = "Instrument|Synth|Sampler";

const VST_CString PlugType_kSpatial				= "Spatial";
const VST_CString PlugType_kSpatialFx			= "Spatial|Fx";
const VST_CString PlugType_kOnlyRealTime		= "OnlyRT";
const VST_CString PlugType_kOnlyOfflineProcess	= "OnlyOfflineProcess";
const VST_CString PlugType_kOnlyARA				= "OnlyARA";

const VST_CString PlugType_kNoOfflineProcess	= "NoOfflineProcess";
const VST_CString PlugType_kUpDownMix			= "Up-Downmix";
const VST_CString PlugType_kAnalyzer			= "Analyzer";
const VST_CString PlugType_kAmbisonics			= "Ambisonics";

const VST_CString PlugType_kMono				= "Mono";
const VST_CString PlugType_kStereo				= "Stereo";
const VST_CString PlugType_kSurround			= "Surround";

#ifdef __cplusplus
}
#endif
