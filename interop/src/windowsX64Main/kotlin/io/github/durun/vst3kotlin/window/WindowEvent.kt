package io.github.durun.vst3kotlin.window

import platform.windows.WM_QUIT
import platform.windows.tagMSG

fun tagMSG.toKEvent(): WindowEvent {
    return when(this.message.toInt()) {
        WM_QUIT -> WindowEvent.OnClosed
        else -> WindowEvent.Other
    }
}