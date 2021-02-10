/**
 * Created by  : Steinberg, 12/2005
 * Modified by : Naoki Ando, 10/02/2021
 */

#pragma once

#include "base/fstrdefs.h"

#ifdef __cplusplus
extern "C" {
#endif

/** VST3 SDK Version */
#ifndef kVstVersionString
#define kVstVersionString "VST 3.7.1"  ///< SDK version for PClassInfo2
#endif

#define kVstVersionMajor 3
#define kVstVersionMinor 7
#define kVstVersionSub 1

#define VST_VERSION ((kVstVersionMajor << 16) | (kVstVersionMinor << 8) | kVstVersionSub)

// Versions History which allows to write such code:
// #if VST_VERSION >= VST_3_6_5_VERSION
#define VST_3_7_1_VERSION 0x030701
#define VST_3_7_0_VERSION 0x030700
#define VST_3_6_14_VERSION 0x03060E
#define VST_3_6_13_VERSION 0x03060D
#define VST_3_6_12_VERSION 0x03060C
#define VST_3_6_11_VERSION 0x03060B
#define VST_3_6_10_VERSION 0x03060A
#define VST_3_6_9_VERSION 0x030609
#define VST_3_6_8_VERSION 0x030608
#define VST_3_6_7_VERSION 0x030607
#define VST_3_6_6_VERSION 0x030606
#define VST_3_6_5_VERSION 0x030605
#define VST_3_6_0_VERSION 0x030600
#define VST_3_5_0_VERSION 0x030500
#define VST_3_1_0_VERSION 0x030100
#define VST_3_0_0_VERSION 0x030000

//------------------------------------------------------------------------
// String Types
//------------------------------------------------------------------------
typedef char16 TChar;          ///< UTF-16 character
typedef TChar String128[128];  ///< 128 character UTF-16 string
typedef const char8* CString;  ///< C-String

//------------------------------------------------------------------------
// General
//------------------------------------------------------------------------
typedef int32 MediaType;      ///< media type (audio/event)
typedef int32 BusDirection;   ///< bus direction (in/out)
typedef int32 BusType;        ///< bus type (main/aux)
typedef int32 IoMode;         ///< I/O mode (see \ref vst3IoMode)
typedef int32 UnitID;         ///< unit identifier
typedef double ParamValue;    ///< parameter value type
typedef uint32 ParamID;       ///< parameter identifier
typedef int32 ProgramListID;  ///< program list identifier
typedef int16 CtrlNumber;     ///< MIDI controller number (see \ref ControllerNumbers for allowed values)

typedef double TQuarterNotes;  ///< time expressed in quarter notes
typedef int64 TSamples;        ///< time expressed in audio samples

typedef uint32 ColorSpec;  ///< color defining by 4 component ARGB value (Alpha/Red/Green/Blue)

//------------------------------------------------------------------------
static const ParamID kNoParamId = 0xffffffff;  ///< default for uninitialized parameter ID
// static const ParamID kNoParamId = std::numeric_limits<ParamID>::max ();

//------------------------------------------------------------------------
// Audio Types
//------------------------------------------------------------------------
typedef float Sample32;     ///< 32-bit precision audio sample
typedef double Sample64;    ///< 64-bit precision audio sample
typedef double SampleRate;  ///< sample rate

//------------------------------------------------------------------------
// Speaker Arrangements Types
//------------------------------------------------------------------------
typedef uint64 SpeakerArrangement;  ///< Bitset of speakers
typedef uint64 Speaker;             ///< Bit for one speaker

#ifdef __cplusplus
}
#endif
