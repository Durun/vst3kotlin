#include "cast.h"

// [IComponentHandler] member functions
tresult PLUGIN_API IComponentHandler_beginEdit(IComponentHandler* this_ptr, ParamID id) {
    return cast(this_ptr)->beginEdit(id);
}
tresult PLUGIN_API IComponentHandler_performEdit(IComponentHandler* this_ptr, ParamID id, ParamValue valueNormalized) {
    return cast(this_ptr)->performEdit(id, valueNormalized);
}
tresult PLUGIN_API IComponentHandler_endEdit(IComponentHandler* this_ptr, ParamID id) {
    return cast(this_ptr)->endEdit(id);
}
tresult PLUGIN_API IComponentHandler_restartComponent(IComponentHandler* this_ptr, int32 flags) {
    return cast(this_ptr)->restartComponent(flags);
}

// [IComponentHandler2] member functions
tresult PLUGIN_API IComponentHandler2_setDirty(IComponentHandler2* this_ptr, TBool state) {
    return cast(this_ptr)->setDirty(state);
}
tresult PLUGIN_API IComponentHandler2_requestOpenEditor(IComponentHandler2* this_ptr, FIDString name) {
    return cast(this_ptr)->requestOpenEditor(name);
}
tresult PLUGIN_API IComponentHandler2_requestOpenEditor_default(IComponentHandler2* this_ptr) {
    return cast(this_ptr)->requestOpenEditor();
}
tresult PLUGIN_API IComponentHandler2_startGroupEdit(IComponentHandler2* this_ptr) {
    return cast(this_ptr)->startGroupEdit();
}
tresult PLUGIN_API IComponentHandler2_finishGroupEdit(IComponentHandler2* this_ptr) {
    return cast(this_ptr)->finishGroupEdit();
}
