#include "cast.h"

// [IPlugView] member functions
tresult PLUGIN_API IPlugView_isPlatformTypeSupported(IPlugView* this_ptr, FIDString type) {
    return cast(this_ptr)->isPlatformTypeSupported(type);
}
tresult PLUGIN_API IPlugView_attached(IPlugView* this_ptr, void* parent, FIDString type) {
    return cast(this_ptr)->attached(parent, type);
}
tresult PLUGIN_API IPlugView_removed(IPlugView* this_ptr) {
    return cast(this_ptr)->removed();
}
tresult PLUGIN_API IPlugView_onWheel(IPlugView* this_ptr, float distance) {
    return cast(this_ptr)->onWheel(distance);
}
tresult PLUGIN_API IPlugView_onKeyDown(IPlugView* this_ptr, char16 key, int16 keyCode, int16 modifiers) {
    return cast(this_ptr)->onKeyDown(key, keyCode, modifiers);
}
tresult PLUGIN_API IPlugView_onKeyUp(IPlugView* this_ptr, char16 key, int16 keyCode, int16 modifiers) {
    return cast(this_ptr)->onKeyUp(key, keyCode, modifiers);
}
tresult PLUGIN_API IPlugView_getSize(IPlugView* this_ptr, ViewRect* size) {
    return cast(this_ptr)->getSize(cast(size));
}
tresult PLUGIN_API IPlugView_onSize(IPlugView* this_ptr, ViewRect* newSize) {
    return cast(this_ptr)->onSize(cast(newSize));
}
tresult PLUGIN_API IPlugView_onFocus(IPlugView* this_ptr, TBool state) {
    return cast(this_ptr)->onFocus(state);
}
tresult PLUGIN_API IPlugView_setFrame(IPlugView* this_ptr, IPlugFrame* frame) {
    return cast(this_ptr)->setFrame(cast(frame));
}
tresult PLUGIN_API IPlugView_canResize(IPlugView* this_ptr) {
    return cast(this_ptr)->canResize();
}
tresult PLUGIN_API IPlugView_checkSizeConstraint(IPlugView* this_ptr, ViewRect* rect) {
    return cast(this_ptr)->checkSizeConstraint(cast(rect));
}

// [IPlugFrame] member functions
tresult PLUGIN_API IPlugFrame_resizeView(IPlugFrame* this_ptr, IPlugView* view, ViewRect* newSize) {
    return cast(this_ptr)->resizeView(cast(view), cast(newSize));
}
