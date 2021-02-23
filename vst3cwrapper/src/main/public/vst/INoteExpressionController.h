#pragma once

#include "base/FUnknown.h"
#include "vsttypes.h"

#ifdef __cplusplus
extern "C" {
#endif


typedef uint32 NoteExpressionTypeID;
typedef double NoteExpressionValue;

enum NoteExpressionTypeIDs {
    kVolumeTypeID = 0,
    kPanTypeID,
    kTuningTypeID,

    kVibratoTypeID,
    kExpressionTypeID,
    kBrightnessTypeID,
    kTextTypeID,
    kPhonemeTypeID,

    kCustomStart = 100000,
    kCustomEnd = 200000,

    kInvalidTypeID = 0xFFFFFFFF
};

typedef struct NoteExpressionValueDescription {
    NoteExpressionValue defaultValue;
    NoteExpressionValue minimum;
    NoteExpressionValue maximum;
    int32 stepCount;
} NoteExpressionValueDescription;

typedef struct NoteExpressionValueEvent {
    NoteExpressionTypeID typeId;
    int32 noteId;
    NoteExpressionValue value;
} NoteExpressionValueEvent;

typedef struct NoteExpressionTextEvent {
    NoteExpressionTypeID typeId;
    int32 noteId;
    uint32 textLen;
    const TChar* text;
} NoteExpressionTextEvent;

enum NoteExpressionTypeFlags {
    kIsBipolar = 1 << 0,
    kIsOneShot = 1 << 1,
    kIsAbsolute = 1 << 2,
    kAssociatedParameterIDValid = 1 << 3,
};
typedef struct NoteExpressionTypeInfo {
    NoteExpressionTypeID typeId;
    String128 title;
    String128 shortTitle;
    String128 units;
    int32 unitId;
    NoteExpressionValueDescription valueDesc;
    ParamID associatedParameterId;

    int32 flags;
} NoteExpressionTypeInfo;


/**
 * inherited from [FUnknown]
 */
typedef struct INoteExpressionController {
	VTable *vtable;
} INoteExpressionController;
const TUID INoteExpressionController_iid = INLINE_UID_c(0xB7F8F859, 0x41234872, 0x91169581, 0x4F3721A3);
// [INoteExpressionController] member functions
int32 PLUGIN_API INoteExpressionController_getNoteExpressionCount(INoteExpressionController* this_ptr, int32 busIndex, int16 channel);
tresult PLUGIN_API INoteExpressionController_getNoteExpressionInfo(INoteExpressionController* this_ptr, int32 busIndex, int16 channel, int32 noteExpressionIndex, NoteExpressionTypeInfo* info);
tresult PLUGIN_API INoteExpressionController_getNoteExpressionStringByValue(INoteExpressionController* this_ptr, int32 busIndex, int16 channel, NoteExpressionTypeID id, NoteExpressionValue valueNormalized, String128 string);
tresult PLUGIN_API INoteExpressionController_getNoteExpressionValueByString(INoteExpressionController* this_ptr, int32 busIndex, int16 channel, NoteExpressionTypeID id, const TChar* string, NoteExpressionValue* valueNormalized);


enum KeyswitchTypeIDs {
    kNoteOnKeyswitchTypeID = 0,
    kOnTheFlyKeyswitchTypeID,
    kOnReleaseKeyswitchTypeID,
    kKeyRangeTypeID
};
typedef uint32 KeyswitchTypeID;
typedef struct KeyswitchInfo {
    KeyswitchTypeID typeId;
    String128 title;
    String128 shortTitle;
    int32 keyswitchMin;
    int32 keyswitchMax;
    int32 keyRemapped;
    int32 unitId;

    int32 flags;
} KeyswitchInfo;


/**
 * inherited from [FUnknown]
 */
typedef struct IKeyswitchController {
	VTable *vtable;
} IKeyswitchController;
const TUID IKeyswitchController_iid = INLINE_UID_c(0x1F2F76D3, 0xBFFB4B96, 0xB99527A5, 0x5EBCCEF4);
// [IKeyswitchController] member functions
int32 PLUGIN_API IKeyswitchController_getKeyswitchCount(IKeyswitchController* this_ptr, int32 busIndex, int16 channel);
tresult PLUGIN_API IKeyswitchController_getKeyswitchInfo(IKeyswitchController* this_ptr, int32 busIndex, int16 channel, int32 keySwitchIndex, KeyswitchInfo* info);


#ifdef __cplusplus
}
#endif
