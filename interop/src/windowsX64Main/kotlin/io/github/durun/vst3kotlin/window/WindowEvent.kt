package io.github.durun.vst3kotlin.window

import platform.windows.tagMSG
import platform.windows.WM_QUIT

fun tagMSG.toKEvent(): WindowEvent {
    return when(this.message.toInt()) {
        WM_QUIT -> WindowEvent.OnClosed
        else -> WindowEvent.Other
    }
}