/**
 * Created by  : Steinberg, 01/2004
 * Modified by : Naoki Ando, 30/01/2021
 */

#pragma once

#include <pluginterfaces/base/fplatform.h>
#include "bool.h"

#ifdef __cplusplus
extern "C" {
#endif


//#define UNICODE_OFF 	// disable / enable unicode

#ifdef UNICODE_OFF
	#ifdef UNICODE
	#undef UNICODE
	#endif
#else
	#define UNICODE 1
#endif

#ifdef UNICODE
#define _UNICODE 1
#endif

//-----------------------------------------------------------------
// Integral Types
	typedef char int8;
	typedef unsigned char uint8;
	typedef unsigned char uchar;

	typedef short int16;
	typedef unsigned short uint16;

#if SMTG_OS_WINDOWS && !defined(__GNUC__)
	typedef long int32;
	typedef unsigned long uint32;
#else
	typedef int int32;
	typedef unsigned int uint32;
#endif

	static const int32 kMaxLong = 0x7fffffff;
	static const int32 kMinLong = (-0x7fffffff - 1);
	static const int32 kMaxInt32 = kMaxLong;
	static const int32 kMinInt32 = kMinLong;
	static const uint32 kMaxInt32u = 0xffffffff;


#if SMTG_OS_WINDOWS && !defined(__GNUC__)
	typedef __int64 int64;
	typedef unsigned __int64 uint64;
	static const int64 kMaxInt64 = 9223372036854775807i64;
	static const int64 kMinInt64 = (-9223372036854775807i64 - 1);
#else
	typedef long long int64;
	typedef unsigned long long uint64;
	static const int64 kMaxInt64 = 0x7fffffffffffffffLL;
	static const int64 kMinInt64 = (-0x7fffffffffffffffLL-1);
#endif
	static const uint64 kMaxInt64u = (uint64) (0xffffffff) | ((uint64) (0xffffffff) << 32);

//-----------------------------------------------------------------
// other Semantic Types
	typedef int64 TSize;   // byte (or other) sizes
	typedef int32 tresult; // result code
//-----------------------------------------------------------------
	static const float kMaxFloat = 3.40282346638528860E38;
	static const double kMaxDouble = 1.7976931348623158E308;

#if SMTG_PLATFORM_64
	typedef uint64 TPtrInt;
#else
	typedef uint32 TPtrInt;
#endif

//------------------------------------------------------------------
// Boolean
	typedef uint8 TBool;

//------------------------------------------------------------------
// Char / Strings
	typedef char char8;
#ifdef _NATIVE_WCHAR_T_DEFINED
	typedef __wchar_t char16;
#elif defined(__MINGW32__)
	typedef wchar_t char16;
#elif SMTG_CPP11
	typedef char16_t char16;
#else
	typedef int16 char16;
#endif

#ifdef UNICODE
	typedef char16 tchar;
#else
	typedef char8 tchar;
#endif

	typedef const char8* CStringA;
	typedef const char16* CStringW;
	typedef const tchar* CString;

	typedef const char8* FIDString; // identifier as string (used for attributes, messages)

	const FIDString kPlatformStringWin = "WIN";
	const FIDString kPlatformStringMac = "MAC";
	const FIDString kPlatformStringIOS = "IOS";
	const FIDString kPlatformStringLinux = "Linux";
#if SMTG_OS_WINDOWS
	const FIDString kPlatformString = kPlatformStringWin;
#elif SMTG_OS_IOS
	const FIDString kPlatformString = kPlatformStringIOS;
#elif SMTG_OS_MACOS
	const FIDString kPlatformString = kPlatformStringMac;
#elif SMTG_OS_LINUX
	const FIDString kPlatformString = kPlatformStringLinux;
#endif

//------------------------------------------------------------------------
/** Coordinates	*/
	typedef int32 UCoord;
	static const UCoord kMaxCoord = ((UCoord)0x7FFFFFFF);
	static const UCoord kMinCoord = ((UCoord)-0x7FFFFFFF);

//----------------------------------------------------------------------------
/** Byte-order Conversion Macros
*/
#define SWAP_32(l) { \
	unsigned char* p = (unsigned char*)& (l); \
	unsigned char t; \
	t = p[0]; p[0] = p[3]; p[3] = t; t = p[1]; p[1] = p[2]; p[2] = t; }

#define SWAP_16(w) { \
	unsigned char* p = (unsigned char*)& (w); \
	unsigned char t; \
	t = p[0]; p[0] = p[1]; p[1] = t; }

#define SWAP_64(i) { \
	unsigned char* p = (unsigned char*)& (i); \
	unsigned char t; \
	t = p[0]; p[0] = p[7]; p[7] = t; t = p[1]; p[1] = p[6]; p[6] = t; \
	t = p[2]; p[2] = p[5]; p[5] = t; t = p[3]; p[3] = p[4]; p[4] = t;}

// always inline macros (only when RELEASE is 1)
//----------------------------------------------------------------------------
#if RELEASE
    #if SMTG_OS_MACOS || SMTG_OS_LINUX || defined(__MINGW32__)
		#define SMTG_ALWAYS_INLINE	__inline__ __attribute__((__always_inline__))
		#define SMTG_NEVER_INLINE __attribute__((noinline))
	#elif SMTG_OS_WINDOWS
		#define SMTG_ALWAYS_INLINE	__forceinline
		#define SMTG_NEVER_INLINE __declspec(noinline)
	#endif
#endif

#ifndef SMTG_ALWAYS_INLINE
	#define SMTG_ALWAYS_INLINE	inline
#endif
#ifndef SMTG_NEVER_INLINE
	#define SMTG_NEVER_INLINE
#endif


#ifdef __cplusplus
}
#endif