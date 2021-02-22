package io.github.durun.vst3kotlin.gui

import cwrapper.*
import io.github.durun.util.CClass
import io.github.durun.vst3kotlin.base.FUnknown
import io.github.durun.vst3kotlin.base.kResultString
import kotlinx.cinterop.*

actual class PlatformView(val ptr: COpaquePointer)

actual class PlugView(thisPtr: CPointer<IPlugView>) : FUnknown(thisPtr), CClass {
	override val ptr: CPointer<IPlugView> get() = thisRawPtr.reinterpret()

	actual fun isPlatformTypeSupported(type: String): Boolean {
		return IPlugView_isPlatformTypeSupported(this.ptr, type) == kResultTrue
	}

	actual fun attached(parent: PlatformView, type: String) {
		val result = IPlugView_attached(this.ptr, parent.ptr, type)
		check(result == kResultTrue) { result.kResultString }
	}

	actual val removed: Boolean get() = IPlugView_removed(this.ptr) == kResultTrue
	actual fun onWheel(distance: Float): Boolean = IPlugView_onWheel(this.ptr, distance) == kResultTrue

	actual fun onKeyDown(key: Short, keyCode: Short, modifiers: Short): Boolean {
		return IPlugView_onKeyDown(this.ptr, key, keyCode, modifiers) == kResultTrue
	}

	actual fun onKeyUp(key: Short, keyCode: Short, modifiers: Short): Boolean {
		return IPlugView_onKeyUp(this.ptr, key, keyCode, modifiers) == kResultTrue
	}

	actual val size: ViewRect
		get() = memScoped {
			val rect = alloc<cwrapper.ViewRect>()
			val result = IPlugView_getSize(this@PlugView.ptr, rect.ptr)
			rect.toKViewRect()
		}

	actual fun onSize(newSize: ViewRect) {
		memScoped {
			val rect = alloc<cwrapper.ViewRect>()
			rect.set(newSize)
			val result = IPlugView_onSize(this@PlugView.ptr, rect.ptr)
			check(result == kResultTrue) { result.kResultString }
		}
	}

	@OptIn(ExperimentalUnsignedTypes::class)
	actual fun onFocus(state: Boolean) {
		val result = IPlugView_onFocus(this.ptr, state.toByte().toUByte())
		check(result == kResultTrue) { result.kResultString }
	}

	actual fun setFrame(frame: PlugFrame) {
		val result = IPlugView_setFrame(this.ptr, frame.ptr)
		check(result == kResultTrue) { result.kResultString }
	}

	actual val canResize: Boolean
		get() = IPlugView_canResize(this.ptr) == kResultTrue

	actual fun checkSizeConstraint(rect: ViewRect): Boolean {
		val result = memScoped {
			val buf = alloc<cwrapper.ViewRect>()
			buf.set(rect)
			IPlugView_checkSizeConstraint(this@PlugView.ptr, buf.ptr)
		}
		return result == kResultTrue
	}

}

actual class PlugFrame(thisPtr: CPointer<IPlugFrame>) : FUnknown(thisPtr), CClass {
	override val ptr: CPointer<IPlugFrame> get() = thisRawPtr.reinterpret()

	actual fun resizeView(view: PlugView, newSize: ViewRect) {
		memScoped {
			val rect = alloc<cwrapper.ViewRect>()
			rect.set(newSize)
			val result = IPlugFrame_resizeView(this@PlugFrame.ptr, view.ptr, rect.ptr)
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