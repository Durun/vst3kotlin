#pragma once

#include "base/ftypes.h"
#include "base/FUID.h"
#include "INoteExpressionController.h"
#include "ProcessContext.h"
#include "vtable.h"

#ifdef __cplusplus
extern "C" {
#endif


enum NoteIDUserRange {
    kNoteIDUserRangeLowerBound = -10000,
    kNoteIDUserRangeUpperBound = -1000,
};

typedef struct NoteOnEvent {
    int16 channel;
    int16 pitch;
    float tuning;
    float velocity;
    int32 length;
    int32 noteId;
} NoteOnEvent;

typedef struct NoteOffEvent {
    int16 channel;
    int16 pitch;
    float velocity;
    int32 noteId;
    float tuning;
} NoteOffEvent;

typedef struct DataEvent {
    uint32 size;
    uint32 type;
    const uint8* bytes;

    /** Value for DataEvent::type */
    enum DataTypes {
        kMidiSysEx = 0
    };
} DataEvent;

typedef struct PolyPressureEvent {
    int16 channel;
    int16 pitch;
    float pressure;
    int32 noteId;
} PolyPressureEvent;

typedef struct ChordEvent {
    int16 root;
    int16 bassNote;
    int16 mask;
    uint16 textLen;

    const TChar* text;
} ChordEvent;

typedef struct ScaleEvent {
    int16 root;
    int16 mask;
    uint16 textLen;

    const TChar* text;
} ScaleEvent;

typedef struct LegacyMIDICCOutEvent {
    uint8 controlNumber;
    int8 channel;
    int8 value;
    int8 value2;
} LegacyMIDICCOutEvent;


enum EventFlags {
    kIsLive = 1 << 0,
    kUserReserved1 = 1 << 14,
    kUserReserved2 = 1 << 15
};
enum EventTypes {
	kNoteOnEvent = 0,
	kNoteOffEvent = 1,
	kDataEvent = 2,
	kPolyPressureEvent = 3,
	kNoteExpressionValueEvent = 4,
	kNoteExpressionTextEvent = 5,
	kChordEvent = 6,
	kScaleEvent = 7,
	kLegacyMIDICCOutEvent = 65535
};
typedef struct Event {
    int32 busIndex;
    int32 sampleOffset;
    TQuarterNotes ppqPosition;

    uint16 flags;

    uint16 type;
    union {
        NoteOnEvent noteOn;
        NoteOffEvent noteOff;
        DataEvent data;
        PolyPressureEvent polyPressure;
        NoteExpressionValueEvent noteExpressionValue;
        NoteExpressionTextEvent noteExpressionText;
        ChordEvent chord;
        ScaleEvent scale;
        LegacyMIDICCOutEvent midiCCOut;
    };
} Event;

/**
 * inherited from [FUnknown]
 */
typedef struct IEventList {
	VTable *vtable;
} IEventList;
const TUID IEventList_iid = INLINE_UID_c(0x3A2C4214, 0x346349FE, 0xB2C4F397, 0xB9695A44);
// [IEventList] member functions
int32 PLUGIN_API IEventList_getEventCount(IEventList* this_ptr);
tresult PLUGIN_API IEventList_getEvent(IEventList* this_ptr, int32 index, Event* e);
tresult PLUGIN_API IEventList_addEvent(IEventList* this_ptr, Event* e);

#ifdef __cplusplus
}
#endif
