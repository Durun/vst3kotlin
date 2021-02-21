package io.github.durun.vst3kotlin.gui

import io.github.durun.util.CClass
import io.github.durun.vst3kotlin.base.FUnknown

data class ViewRect(
	val left: Int,
	val top: Int,
	val right: Int,
	val bottom:Int
)

expect class PlatformView

expect class PlugView : FUnknown, CClass {
	fun isPlatformTypeSupported(type: String): Boolean
	fun attached(parent: PlatformView, type: String)
	val removed: Boolean
	fun onWheel(distance: Float): Boolean
	fun onKeyDown(key: Char, keyCode: Short, modifiers: Short): Boolean
	fun onKeyUp(key: Char, keyCode: Short, modifiers: Short): Boolean
	val size: ViewRect
	fun onSize(newSize: ViewRect)
	fun onFocus(state: Boolean)
	fun setFrame(frame: PlugFrame)
	val canResize: Boolean
	fun checkSizeConstraint(rect: ViewRect): Boolean
}

expect class PlugFrame : FUnknown, CClass {
	fun resizeView(view: PlugView, newSize: ViewRect)
}