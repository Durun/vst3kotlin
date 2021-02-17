#pragma once

#include "cast.h"

// [IPlugView] member functions
tresult PLUGIN_API isPlatformTypeSupported(IPlugView* this_ptr, FIDString type) {
    return cast(this_ptr)->isPlatformTypeSupported(type);
}
tresult PLUGIN_API attached(IPlugView* this_ptr, void* parent, FIDString type) {
    return cast(this_ptr)->attached(parent, type);
}
tresult PLUGIN_API removed(IPlugView* this_ptr) {
    return cast(this_ptr)->removed();
}
tresult PLUGIN_API onWheel(IPlugView* this_ptr, float distance) {
    return cast(this_ptr)->onWheel(distance);
}
tresult PLUGIN_API onKeyDown(IPlugView* this_ptr, char16 key, int16 keyCode, int16 modifiers) {
    return cast(this_ptr)->onKeyDown(key, keyCode, modifiers);
}
tresult PLUGIN_API onKeyUp(IPlugView* this_ptr, char16 key, int16 keyCode, int16 modifiers) {
    return cast(this_ptr)->onKeyUp(key, keyCode, modifiers);
}
tresult PLUGIN_API getSize(IPlugView* this_ptr, ViewRect* size) {
    return cast(this_ptr)->getSize(cast(size));
}
tresult PLUGIN_API onSize(IPlugView* this_ptr, ViewRect* newSize) {
    return cast(this_ptr)->onSize(cast(newSize));
}
tresult PLUGIN_API onFocus(IPlugView* this_ptr, TBool state) {
    return cast(this_ptr)->onFocus(state);
}
tresult PLUGIN_API setFrame(IPlugView* this_ptr, IPlugFrame* frame) {
    return cast(this_ptr)->setFrame(cast(frame));
}
tresult PLUGIN_API canResize(IPlugView* this_ptr) {
    return cast(this_ptr)->canResize();
}
tresult PLUGIN_API checkSizeConstraint(IPlugView* this_ptr, ViewRect* rect) {
    return cast(this_ptr)->checkSizeConstraint(cast(rect));
}

// [IPlugFrame] member functions
tresult PLUGIN_API resizeView(IPlugFrame* this_ptr, IPlugView* view, ViewRect* newSize) {
    return cast(this_ptr)->resizeView(cast(view), cast(newSize));
}
