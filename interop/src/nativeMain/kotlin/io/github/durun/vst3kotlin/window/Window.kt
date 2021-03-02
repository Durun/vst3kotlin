package io.github.durun.vst3kotlin.window

import io.github.durun.data.Vec2
import kotlinx.cinterop.COpaquePointer

expect class Window {
	companion object {
		val platformType: String
		fun create(size: Vec2<Int>, name: String): Window
	}

	val ptr: COpaquePointer
	fun resize(width: Int, height: Int)
	fun show()
	fun loop(continueNext: (WindowEvent) -> Boolean)
}

