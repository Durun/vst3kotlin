package io.github.durun.vst3kotlin.gui

import cwrapper.*
import io.github.durun.vst3kotlin.base.FUnknown
import io.github.durun.vst3kotlin.base.kResultString
import kotlinx.cinterop.*

actual class PlatformView(val ptr: COpaquePointer)

actual class PlugView(thisPtr: CPointer<IPlugView>) : FUnknown(thisPtr) {
	private val thisPtr: CPointer<IPlugView> get() = thisRawPtr.reinterpret()
	val ptr: CPointer<IPlugView> get() = thisPtr

	actual fun isPlatformTypeSupported(type: String): Boolean {
		return IPlugView_isPlatformTypeSupported(thisPtr, type) == kResultTrue
	}

	actual fun attached(parent: PlatformView, type: String) {
		val result = IPlugView_attached(thisPtr, parent.ptr, type)
		check(result == kResultTrue) { result.kResultString }
	}

	actual val removed: Boolean get() = IPlugView_removed(thisPtr) == kResultTrue
	actual fun onWheel(distance: Float): Boolean = IPlugView_onWheel(thisPtr, distance) == kResultTrue

	actual fun onKeyDown(key: Char, keyCode: Short, modifiers: Short): Boolean {
		return IPlugView_onKeyDown(thisPtr, key.toShort(), keyCode, modifiers) == kResultTrue
	}

	actual fun onKeyUp(key: Char, keyCode: Short, modifiers: Short): Boolean {
		return IPlugView_onKeyUp(thisPtr, key.toShort(), keyCode, modifiers) == kResultTrue
	}

	actual val size: ViewRect
		get() = memScoped {
			val rect = alloc<cwrapper.ViewRect>()
			val result = IPlugView_getSize(thisPtr, rect.ptr)
			rect.toKViewRect()
		}

	actual fun onSize(newSize: ViewRect) {
		memScoped {
			val rect = alloc<cwrapper.ViewRect>()
			rect.set(newSize)
			val result = IPlugView_onSize(thisPtr, rect.ptr)
			check(result == kResultTrue) { result.kResultString }
		}
	}

	@OptIn(ExperimentalUnsignedTypes::class)
	actual fun onFocus(state: Boolean) {
		val result = IPlugView_onFocus(thisPtr, state.toByte().toUByte())
		check(result == kResultTrue) { result.kResultString }
	}

	actual fun setFrame(frame: PlugFrame) {
		val result = IPlugView_setFrame(thisPtr, frame.ptr)
		check(result == kResultTrue) { result.kResultString }
	}

	actual val canResize: Boolean
		get() = IPlugView_canResize(thisPtr) == kResultTrue

	actual fun checkSizeConstraint(rect: ViewRect): Boolean {
		val result = memScoped {
			val buf = alloc<cwrapper.ViewRect>()
			buf.set(rect)
			IPlugView_checkSizeConstraint(thisPtr, buf.ptr)
		}
		return result == kResultTrue
	}

}

actual class PlugFrame(thisPtr: CPointer<IPlugFrame>) : FUnknown(thisPtr) {
	private val thisPtr: CPointer<IPlugFrame> get() = thisRawPtr.reinterpret()
	val ptr: CPointer<IPlugFrame> get() = thisPtr

	actual fun resizeView(view: PlugView, newSize: ViewRect) {
		memScoped {
			val rect = alloc<cwrapper.ViewRect>()
			rect.set(newSize)
			val result = IPlugFrame_resizeView(thisPtr, view.ptr, rect.ptr)
			check(result == kResultTrue) { result.kResultString }
		}
	}

}

private fun cwrapper.ViewRect.toKViewRect(): ViewRect {
	return ViewRect(left, top, right, bottom)
}

private fun cwrapper.ViewRect.set(value: ViewRect) {
	left = value.left
	top = value.top
	right = value.right
	bottom = value.bottom
}