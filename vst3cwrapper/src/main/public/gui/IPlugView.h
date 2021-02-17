
#pragma once

#include "base/FUnknown.h"

#ifdef __cplusplus
extern "C" {
#endif

typedef struct ViewRect {
    int32 left;
    int32 top;
    int32 right;
    int32 bottom;
} ViewRect;

const FIDString kPlatformTypeHWND = "HWND";  ///< HWND handle. (Microsoft Windows)
const FIDString kPlatformTypeHIView = "HIView";  ///< HIViewRef. (Mac OS X)
const FIDString kPlatformTypeNSView = "NSView";  ///< NSView pointer. (Mac OS X)
const FIDString kPlatformTypeUIView = "UIView";  ///< UIView pointer. (iOS)
const FIDString kPlatformTypeX11EmbedWindowID = "X11EmbedWindowID";  ///< X11 Window ID. (X11)

/**
 * inherited from [FUnknown]
 */
typedef struct IPlugFrame {
    VTable* vtable;
} IPlugFrame;
const TUID IPlugFrame_iid = INLINE_UID_c(0x367FAF01, 0xAFA94693, 0x8D4DA2A0, 0xED0882A3);

/**
 * inherited from [FUnknown]
 */
typedef struct IPlugView {
    VTable* vtable;
} IPlugView;
const TUID IPlugView_iid = INLINE_UID_c(0x5BC32507, 0xD06049EA, 0xA6151B52, 0x2B755B29);
// [IPlugView] member functions
tresult PLUGIN_API IPlugView_isPlatformTypeSupported(IPlugView* this_ptr, FIDString type);
tresult PLUGIN_API IPlugView_attached(IPlugView* this_ptr, void* parent, FIDString type);
tresult PLUGIN_API IPlugView_removed(IPlugView* this_ptr);
tresult PLUGIN_API IPlugView_onWheel(IPlugView* this_ptr, float distance);
tresult PLUGIN_API IPlugView_onKeyDown(IPlugView* this_ptr, char16 key, int16 keyCode, int16 modifiers);
tresult PLUGIN_API IPlugView_onKeyUp(IPlugView* this_ptr, char16 key, int16 keyCode, int16 modifiers);
tresult PLUGIN_API IPlugView_getSize(IPlugView* this_ptr, ViewRect* size);
tresult PLUGIN_API IPlugView_onSize(IPlugView* this_ptr, ViewRect* newSize);
tresult PLUGIN_API IPlugView_onFocus(IPlugView* this_ptr, TBool state);
tresult PLUGIN_API IPlugView_setFrame(IPlugView* this_ptr, IPlugFrame* frame);
tresult PLUGIN_API IPlugView_canResize(IPlugView* this_ptr);
tresult PLUGIN_API IPlugView_checkSizeConstraint(IPlugView* this_ptr, ViewRect* rect);

// [IPlugFrame] member functions
tresult PLUGIN_API IPlugFrame_resizeView(IPlugFrame* this_ptr, IPlugView* view, ViewRect* newSize);


#ifdef __cplusplus
}
#endif
