#pragma once

#include "FUID.h"
#include "FUnknown.h"
#include "IPluginFactory.h"
#include "PClassInfo.h"
#include "PFactoryInfo.h"
#include "ftypes.h"
#include "vtable.h"

#ifdef __cplusplus
extern "C" {
#endif

/**
 * inherited from [FUnknown]
 */
typedef struct IPluginFactory {
	VTable *vtable;
} IPluginFactory;
const TUID IPluginFactory_iid = INLINE_UID_c(0x7A4D811C, 0x52114A1F, 0xAED9D2EE, 0x0B43BF9F);

// [IPluginFactory] member functions
tresult PLUGIN_API IPluginFactory_queryInterface(IPluginFactory* this_ptr, const TUID _iid, void** obj);
uint32 PLUGIN_API IPluginFactory_addRef(IPluginFactory* this_ptr);
uint32 PLUGIN_API IPluginFactory_release(IPluginFactory* this_ptr);
tresult PLUGIN_API IPluginFactory_getFactoryInfo(IPluginFactory* this_ptr, PFactoryInfo* info);
int32 PLUGIN_API IPluginFactory_countClasses(IPluginFactory* this_ptr);
tresult PLUGIN_API IPluginFactory_getClassInfo(IPluginFactory* this_ptr, int32 index, PClassInfo* info);
tresult PLUGIN_API IPluginFactory_createInstance(IPluginFactory* this_ptr, FIDString cid, FIDString _iid, void** obj);

typedef IPluginFactory*(PLUGIN_API* IPluginFactoryGetter)(void);

// version 2 [IPluginFactory2]
typedef struct IPluginFactory2 {
	VTable* vtable;
} IPluginFactory2;
const TUID IPluginFactory2_iid = INLINE_UID_c(0x0007B650, 0xF24B4C0B, 0xA464EDB9, 0xF00B2ABB);

// [IPluginFactory2] member functions
tresult PLUGIN_API getClassInfo2(int32 index, PClassInfo2* info);

// version 3 [IPluginFactory3]
typedef struct IPluginFactory3 {
	VTable* vtable;
} IPluginFactory3;
const TUID IPluginFactory3_iid = INLINE_UID_c(0x4555A2AB, 0xC1234E57, 0x9B122910, 0x36878931);

// [IPluginFactory3] member functions
tresult PLUGIN_API getClassInfoUnicode(int32 index, PClassInfoW* info);
tresult PLUGIN_API setHostContext(FUnknown* context);

#ifdef __cplusplus
}
#endif
