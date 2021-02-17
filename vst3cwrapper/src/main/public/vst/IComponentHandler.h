#pragma once

#include "base/FUnknown.h"
#include "vsttypes.h"

#ifdef __cplusplus
extern "C" {
#endif

typedef struct ParameterInfo {
    ParamID id;
    String128 title;
    String128 shortTitle;
    String128 units;
    int32 stepCount;

    ParamValue defaultNormalizedValue;
    UnitID unitId;

    int32 flags;
} ParameterInfo;
enum ParameterFlags {
    //kNoFlags = 0,
    kCanAutomate = 1 << 0,
    kIsReadOnly = 1 << 1,
    kIsWrapAround = 1 << 2,
    kIsList = 1 << 3,
    kIsHidden = 1 << 4,


    kIsProgramChange = 1 << 15,

    kIsBypass = 1 << 16

};

const FIDString kEditor = "editor";

enum RestartFlags {
    kReloadComponent = 1 << 0,
    kIoChanged = 1 << 1,
    kParamValuesChanged = 1 << 2,
    kLatencyChanged = 1 << 3,
    kParamTitlesChanged = 1 << 4,
    kMidiCCAssignmentChanged = 1 << 5,
    kNoteExpressionChanged = 1 << 6,
    kIoTitlesChanged = 1 << 7,
    kPrefetchableSupportChanged = 1 << 8,
    kRoutingInfoChanged = 1 << 9
};

/**
 * inherited from [FUnknown]
 */
typedef struct IComponentHandler {
	VTable *vtable;
} IComponentHandler;
const TUID IComponentHandler_iid = INLINE_UID_c(0x93A0BEA3, 0x0BD045DB, 0x8E890B0C, 0xC1E46AC6);
// [IComponentHandler] member functions
tresult PLUGIN_API beginEdit(IComponentHandler* this_ptr, ParamID id);
tresult PLUGIN_API performEdit(IComponentHandler* this_ptr, ParamID id, ParamValue valueNormalized);
tresult PLUGIN_API endEdit(IComponentHandler* this_ptr, ParamID id);
tresult PLUGIN_API restartComponent(IComponentHandler* this_ptr, int32 flags);

/**
 * inherited from [FUnknown]
 */
// version 2 [IComponentHandler2]
typedef struct IComponentHandler2 {
    VTable *vtable;
} IComponentHandler2;
const TUID IComponentHandler2_iid = INLINE_UID_c(0xF040B4B3, 0xA36045EC, 0xABCDC045, 0xB4D5A2CC);
// [IComponentHandler2] member functions
tresult PLUGIN_API setDirty(IComponentHandler2* this_ptr, TBool state);
tresult PLUGIN_API requestOpenEditor(IComponentHandler2* this_ptr, FIDString name);
tresult PLUGIN_API requestOpenEditor_default(IComponentHandler2* this_ptr);
tresult PLUGIN_API startGroupEdit(IComponentHandler2* this_ptr);
tresult PLUGIN_API finishGroupEdit(IComponentHandler2* this_ptr);

#ifdef __cplusplus
}
#endif
