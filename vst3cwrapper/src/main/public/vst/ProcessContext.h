#pragma once

#include "base/FUnknown.h"
#include "vsttypes.h"

#ifdef __cplusplus
extern "C" {
#endif

enum FrameRateFlags {
    kPullDownRate = 1 << 0,
    kDropRate = 1 << 1
};
typedef struct FrameRate {
    uint32 framesPerSecond;
    uint32 flags;
} FrameRate;

enum Masks {
    kChordMask = 0x0FFF,
    kReservedMask = 0xF000
};
typedef struct Chord {
    uint8 keyNote;
    uint8 rootNote;

    int16 chordMask;
} Chord;


enum StatesAndFlags {
    kPlaying = 1 << 1,
    kCycleActive = 1 << 2,
    kRecording = 1 << 3,

    kSystemTimeValid = 1 << 8,
    kContTimeValid = 1 << 17,

    kProjectTimeMusicValid = 1 << 9,
    kBarPositionValid = 1 << 11,
    kCycleValid = 1 << 12,

    kTempoValid = 1 << 10,
    kTimeSigValid = 1 << 13,
    kChordValid = 1 << 18,

    kSmpteValid = 1 << 14,
    kClockValid = 1 << 15
};
typedef struct ProcessContext {
    uint32 state;

    double sampleRate;
    TSamples projectTimeSamples;

    int64 systemTime;
    TSamples continousTimeSamples;

    TQuarterNotes projectTimeMusic;
    TQuarterNotes barPositionMusic;
    TQuarterNotes cycleStartMusic;
    TQuarterNotes cycleEndMusic;

    double tempo;
    int32 timeSigNumerator;
    int32 timeSigDenominator;

    Chord chord;

    int32 smpteOffsetSubframes;
    FrameRate frameRate;

    int32 samplesToNextClock;
} ProcessContext;


#ifdef __cplusplus
}
#endif
