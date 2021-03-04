package io.github.durun.window

import io.github.durun.data.Vec2
import io.github.durun.data.toUInt
import io.github.durun.util.logger
import kotlinx.cinterop.*

actual class Window {
	actual val ptr: COpaquePointer = TODO()
	actual fun show() {
		TODO()
	}

	actual fun loop(continueNext: (WindowEvent) -> Boolean) {
		TODO()
	}

	actual fun resize(size: Vec2<Int>) {
		TODO()
	}

	actual companion object {
		private val log by logger()
		actual val platformType: String = "HIView"
		actual fun create(size: Vec2<Int>, name: String): Window = TODO()
	}
}