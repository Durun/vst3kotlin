package io.github.durun.window

sealed class WindowEvent {
	object OnClosed : WindowEvent()
	object Other : WindowEvent()
}


