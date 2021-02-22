#pragma once

#include "ftypes.h"

#if COM_COMPATIBLE
#if SMTG_OS_WINDOWS
enum {
    kNoInterface = (tresult) (0x80004002L),
    kResultOk = (tresult) (0x00000000L),
    kResultTrue = kResultOk,
    kResultFalse = (tresult) (0x00000001L),
    kInvalidArgument = (tresult) (0x80070057L),
    kNotImplemented = (tresult) (0x80004001L),
    kInternalError = (tresult) (0x80004005L),
    kNotInitialized = (tresult) (0x8000FFFFL),
    kOutOfMemory = (tresult) (0x8007000EL)
};
#else
enum {
    kNoInterface = (tresult) (0x80000004L),
    kResultOk = (tresult) (0x00000000L),
    kResultTrue = kResultOk,
    kResultFalse = (tresult) (0x00000001L),
    kInvalidArgument = (tresult) (0x80000003L),
    kNotImplemented = (tresult) (0x80000001L),
    kInternalError = (tresult) (0x80000008L),
    kNotInitialized = (tresult) (0x8000FFFFL),
    kOutOfMemory = (tresult) (0x80000002L)
};
#endif
#else
enum {
    kNoInterface = -1,
    kResultOk,
    kResultTrue = kResultOk,
    kResultFalse,
    kInvalidArgument,
    kNotImplemented,
    kInternalError,
    kNotInitialized,
    kOutOfMemory
};
#endif
