#pragma once

#include "FUID.h"
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
tresult IPluginFactory_queryInterface(IPluginFactory* this_ptr, const TUID _iid, void** obj);
uint32 IPluginFactory_addRef(IPluginFactory* this_ptr);
uint32 IPluginFactory_release(IPluginFactory* this_ptr);
tresult IPluginFactory_getFactoryInfo(IPluginFactory* this_ptr, PFactoryInfo* info);
int32 IPluginFactory_countClasses(IPluginFactory* this_ptr);
tresult IPluginFactory_getClassInfo(IPluginFactory* this_ptr, int32 index, PClassInfo* info);
tresult IPluginFactory_createInstance(IPluginFactory* this_ptr, FIDString cid, FIDString _iid, void** obj);

typedef IPluginFactory*(PLUGIN_API* IPluginFactoryGetter)(void);

#ifdef __cplusplus
}
#endif
