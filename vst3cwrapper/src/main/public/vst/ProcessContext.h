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
struct FrameRate {
    uint32 framesPerSecond;
    uint32 flags;
};

enum Masks {
    kChordMask = 0x0FFF,
    kReservedMask = 0xF000
};
struct Chord {
    uint8 keyNote;
    uint8 rootNote;

    int16 chordMask;
};


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
struct ProcessContext {
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
};


#ifdef __cplusplus
}
#endif
