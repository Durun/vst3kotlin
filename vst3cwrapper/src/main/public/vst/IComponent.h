
#pragma once

#include "base/FUID.h"
#include "base/FUnknown.h"
#include "base/IBStream.h"
#include "base/PFactoryInfo.h"
#include "vsttypes.h"
#include "vtable.h"

#ifdef __cplusplus
extern "C" {
#endif

const int32 kDefaultFactoryFlags = kUnicode;

enum MediaTypes {
	kAudio = 0,
	kEvent,
	kNumMediaTypes
};

enum BusDirections {
	kInput = 0,
	kOutput
};

enum BusTypes {
	kMain = 0,
	kAux
};

enum BusFlags {
	kDefaultActive = 1 << 0,
	kIsControlVoltage = 1 << 1
};

struct BusInfo {
	MediaType mediaType;
	BusDirection direction;
	int32 channelCount;
	String128 name;
	BusType busType;
	uint32 flags;
};

enum IoModes {
	kSimple = 0,
	kAdvanced,
	kOfflineProcessing
};

struct RoutingInfo {
	MediaType mediaType;
	int32 busIndex;
	int32 channel;
};


/**
 * inherited from [IPluginBase]
 */
typedef struct IComponent {
	VTable *vtable;
} IComponent;
const TUID IComponent_iid = INLINE_UID_c(0xE831FF31, 0xF2D54301, 0x928EBBEE, 0x25697802);

// [IPluginBase] member functions
tresult PLUGIN_API initialize(IComponent* this_ptr, FUnknown* context);
tresult PLUGIN_API terminate(IComponent* this_ptr);
// [IComponent] member functions
tresult PLUGIN_API getControllerClassId(IComponent* this_ptr, TUID classId);
tresult PLUGIN_API setIoMode(IComponent* this_ptr, IoMode mode);
int32 PLUGIN_API getBusCount(IComponent* this_ptr, MediaType type, BusDirection dir);
tresult PLUGIN_API getBusInfo(IComponent* this_ptr, MediaType type, BusDirection dir, int32 index, BusInfo& bus);
tresult PLUGIN_API getRoutingInfo(IComponent* this_ptr, RoutingInfo& inInfo, RoutingInfo& outInfo);
tresult PLUGIN_API activateBus(IComponent* this_ptr, MediaType type, BusDirection dir, int32 index, TBool state);
tresult PLUGIN_API setActive(IComponent* this_ptr, TBool state);
tresult PLUGIN_API setState(IComponent* this_ptr, IBStream* state);
tresult PLUGIN_API getState(IComponent* this_ptr, IBStream* state);

#ifdef __cplusplus
}
#endif
