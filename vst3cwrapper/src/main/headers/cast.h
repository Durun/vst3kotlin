#pragma once

#include <pluginterfaces/base/funknown.h>
#include <pluginterfaces/base/ibstream.h>
#include <pluginterfaces/base/ipluginbase.h>
#include <pluginterfaces/gui/iplugview.h>
#include <pluginterfaces/vst/ivstaudioprocessor.h>
#include <pluginterfaces/vst/ivstcomponent.h>
#include <pluginterfaces/vst/ivstevents.h>
#include <pluginterfaces/vst/ivstnoteexpression.h>
#include <pluginterfaces/vst/ivstparameterchanges.h>

#include "base/FUID.h"
#include "base/IBStream.h"
#include "base/IPluginBase.h"
#include "base/IPluginFactory.h"
#include "gui/IPlugView.h"
#include "vst/IAudioProcessor.h"
#include "vst/IComponent.h"
#include "vst/IEventList.h"
#include "vst/INoteExpressionController.h"
#include "vst/IParameterChanges.h"

Steinberg::FUID* cast(FUID* this_ptr);
const Steinberg::FUID* cast(const FUID* this_ptr);
FUID* cast(Steinberg::FUID* this_ptr);
Steinberg::FUnknown* cast(FUnknown* this_ptr);
Steinberg::IPluginFactory* PLUGIN_API cast(IPluginFactory* this_ptr);
Steinberg::IPluginFactory2* PLUGIN_API cast2(IPluginFactory2* this_ptr);
Steinberg::IPluginFactory3* PLUGIN_API cast3(IPluginFactory3* this_ptr);
Steinberg::IBStream* cast(IBStream* this_ptr);
Steinberg::IPluginBase* PLUGIN_API cast(IPluginBase* this_ptr);
Steinberg::ISizeableStream* cast(ISizeableStream* this_ptr);
Steinberg::IPlugView* cast(IPlugView* this_ptr);
Steinberg::IPlugFrame* cast(IPlugFrame* this_ptr);
Steinberg::ViewRect* cast(ViewRect* this_ptr);
Steinberg::Vst::IAudioProcessor* cast(IAudioProcessor* this_ptr);
Steinberg::Vst::IAudioPresentationLatency* cast(IAudioPresentationLatency* this_ptr);
Steinberg::Vst::IProcessContextRequirements* cast(IProcessContextRequirements* this_ptr);
Steinberg::Vst::ProcessSetup* cast(ProcessSetup* this_ptr);
Steinberg::Vst::ProcessData* cast(ProcessData* this_ptr);
Steinberg::Vst::IComponent* cast(IComponent* this_ptr);
Steinberg::Vst::IEventList* cast(IEventList* this_ptr);
Steinberg::Vst::Event* cast(Event* this_ptr);
Steinberg::Vst::INoteExpressionController* cast(INoteExpressionController* this_ptr);
Steinberg::Vst::IKeyswitchController* cast(IKeyswitchController* this_ptr);
Steinberg::Vst::NoteExpressionTypeInfo* cast(NoteExpressionTypeInfo* this_ptr);
Steinberg::Vst::KeyswitchInfo* cast(KeyswitchInfo* this_ptr);
Steinberg::Vst::IParamValueQueue* cast(IParamValueQueue* this_ptr);
Steinberg::Vst::IParameterChanges* cast(IParameterChanges* this_ptr);
IParamValueQueue* cast(Steinberg::Vst::IParamValueQueue* this_ptr);
