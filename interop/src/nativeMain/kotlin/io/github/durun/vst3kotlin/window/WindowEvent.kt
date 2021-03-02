package io.github.durun.vst3kotlin.window

sealed class WindowEvent {
	object OnClosed : WindowEvent()
	object Other : WindowEvent()
}


