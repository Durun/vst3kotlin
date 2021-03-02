package io.github.durun.vst3kotlin.window

import x11.DestroyNotify
import x11.XEvent

fun XEvent.toKEvent(): WindowEvent {
	return when (this.type) {
		DestroyNotify -> WindowEvent.OnClosed
		else -> WindowEvent.Other
	}
}