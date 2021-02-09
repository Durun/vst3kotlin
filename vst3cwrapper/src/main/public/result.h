#pragma once

#include "ftypes.h"

#if COM_COMPATIBLE
#if SMTG_OS_WINDOWS
enum {
	kNoInterface = static_cast<tresult>(0x80004002L),
	kResultOk = static_cast<tresult>(0x00000000L),
	kResultTrue = kResultOk,
	kResultFalse = static_cast<tresult>(0x00000001L),
	kInvalidArgument = static_cast<tresult>(0x80070057L),
	kNotImplemented = static_cast<tresult>(0x80004001L),
	kInternalError = static_cast<tresult>(0x80004005L),
	kNotInitialized = static_cast<tresult>(0x8000FFFFL),
	kOutOfMemory = static_cast<tresult>(0x8007000EL)
};
#else
enum {
	kNoInterface = static_cast<tresult>(0x80000004L),
	kResultOk = static_cast<tresult>(0x00000000L),
	kResultTrue = kResultOk,
	kResultFalse = static_cast<tresult>(0x00000001L),
	kInvalidArgument = static_cast<tresult>(0x80000003L),
	kNotImplemented = static_cast<tresult>(0x80000001L),
	kInternalError = static_cast<tresult>(0x80000008L),
	kNotInitialized = static_cast<tresult>(0x8000FFFFL),
	kOutOfMemory = static_cast<tresult>(0x80000002L)
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
