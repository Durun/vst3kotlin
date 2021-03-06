#pragma once

#include "base/FUnknown.h"
#include "base/IBStream.h"
#include "gui/IPlugView.h"
#include "vsttypes.h"
#include "IComponentHandler.h"

#ifdef __cplusplus
extern "C" {
#endif

/**
 * inherited from [IPluginBase]
 */
typedef struct IEditController {
    VTable* vtable;
} IEditController;
const TUID IEditController_iid = INLINE_UID_c(0xDCD7BBE3, 0x7742448D, 0xA874AACC, 0x979C759E);
// [IEditController] member functions
tresult PLUGIN_API IEditController_setComponentState(IEditController* this_ptr, IBStream* state);
tresult PLUGIN_API IEditController_setState(IEditController* this_ptr, IBStream* state);
tresult PLUGIN_API IEditController_getState(IEditController* this_ptr, IBStream* state);
int32 PLUGIN_API IEditController_getParameterCount(IEditController* this_ptr);
tresult PLUGIN_API IEditController_getParameterInfo(IEditController* this_ptr, int32 paramIndex, ParameterInfo* info /*out*/);
tresult PLUGIN_API IEditController_getParamStringByValue(IEditController* this_ptr, ParamID id, ParamValue valueNormalized /*in*/, String128 string /*out*/);
tresult PLUGIN_API IEditController_getParamValueByString(IEditController* this_ptr, ParamID id, TChar* string /*in*/, ParamValue* valueNormalized /*out*/);
ParamValue PLUGIN_API IEditController_normalizedParamToPlain(IEditController* this_ptr, ParamID id, ParamValue valueNormalized);
ParamValue PLUGIN_API IEditController_plainParamToNormalized(IEditController* this_ptr, ParamID id, ParamValue plainValue);
ParamValue PLUGIN_API IEditController_getParamNormalized(IEditController* this_ptr, ParamID id);
tresult PLUGIN_API IEditController_setParamNormalized(IEditController* this_ptr, ParamID id, ParamValue value);
tresult PLUGIN_API IEditController_setComponentHandler(IEditController* this_ptr, IComponentHandler* handler);
IPlugView* PLUGIN_API IEditController_createView(IEditController* this_ptr, FIDString name);

typedef enum KnobMode {
    kCircularMode = 0,     ///< Circular with jump to clicked position
    kRelativCircularMode,  ///< Circular without jump to clicked position
    kLinearMode            ///< Linear: depending on vertical movement
} KnobMode;

/**
 * inherited from [FUnknown]
 */
typedef struct IEditController2 {
	VTable *vtable;
} IEditController2;
const TUID IEditController2_iid = INLINE_UID_c(0x7F4EFE59, 0xF3204967, 0xAC27A3AE, 0xAFB63038);
// [IEditController2] member functions
tresult PLUGIN_API IEditController2_setKnobMode(IEditController2* this_ptr, KnobMode mode);
tresult PLUGIN_API IEditController2_openHelp(IEditController2* this_ptr, TBool onlyCheck);
tresult PLUGIN_API IEditController2_openAboutBox(IEditController2* this_ptr, TBool onlyCheck);

#ifdef __cplusplus
}
#endif
