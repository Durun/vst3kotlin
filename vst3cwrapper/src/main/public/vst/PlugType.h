#pragma once

#include "base/ftypes.h"

#ifdef __cplusplus
extern "C" {
#endif

const CString PlugType_kFxAnalyzer			= "Fx|Analyzer";
const CString PlugType_kFxDelay				= "Fx|Delay";
const CString PlugType_kFxDistortion		= "Fx|Distortion";
const CString PlugType_kFxDynamics			= "Fx|Dynamics";
const CString PlugType_kFxEQ				= "Fx|EQ";
const CString PlugType_kFxFilter			= "Fx|Filter";
const CString PlugType_kFx					= "Fx";
const CString PlugType_kFxInstrument		= "Fx|Instrument";
const CString PlugType_kFxInstrumentExternal= "Fx|Instrument|External";
const CString PlugType_kFxSpatial			= "Fx|Spatial";
const CString PlugType_kFxGenerator			= "Fx|Generator";
const CString PlugType_kFxMastering			= "Fx|Mastering";
const CString PlugType_kFxModulation		= "Fx|Modulation";
const CString PlugType_kFxPitchShift		= "Fx|Pitch Shift";
const CString PlugType_kFxRestoration		= "Fx|Restoration";
const CString PlugType_kFxReverb			= "Fx|Reverb";
const CString PlugType_kFxSurround			= "Fx|Surround";
const CString PlugType_kFxTools				= "Fx|Tools";
const CString PlugType_kFxNetwork			= "Fx|Network";

const CString PlugType_kInstrument			= "Instrument";
const CString PlugType_kInstrumentDrum		= "Instrument|Drum";
const CString PlugType_kInstrumentExternal	= "Instrument|External";
const CString PlugType_kInstrumentPiano		= "Instrument|Piano";
const CString PlugType_kInstrumentSampler	= "Instrument|Sampler";
const CString PlugType_kInstrumentSynth		= "Instrument|Synth";
const CString PlugType_kInstrumentSynthSampler = "Instrument|Synth|Sampler";

const CString PlugType_kSpatial				= "Spatial";
const CString PlugType_kSpatialFx			= "Spatial|Fx";
const CString PlugType_kOnlyRealTime		= "OnlyRT";
const CString PlugType_kOnlyOfflineProcess	= "OnlyOfflineProcess";
const CString PlugType_kOnlyARA				= "OnlyARA";

const CString PlugType_kNoOfflineProcess	= "NoOfflineProcess";
const CString PlugType_kUpDownMix			= "Up-Downmix";
const CString PlugType_kAnalyzer			= "Analyzer";
const CString PlugType_kAmbisonics			= "Ambisonics";

const CString PlugType_kMono				= "Mono";
const CString PlugType_kStereo				= "Stereo";
const CString PlugType_kSurround			= "Surround";

#ifdef __cplusplus
}
#endif
