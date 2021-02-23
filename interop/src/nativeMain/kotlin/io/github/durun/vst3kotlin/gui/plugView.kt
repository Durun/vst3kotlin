package io.github.durun.vst3kotlin.gui

import cwrapper.*
import io.github.durun.util.CClass
import io.github.durun.vst3kotlin.Adapter
import io.github.durun.vst3kotlin.base.FUnknown
import io.github.durun.vst3kotlin.base.kResultString
import kotlinx.cinterop.*

class PlatformView(val ptr: COpaquePointer)

class PlugView(thisPtr: CPointer<IPlugView>) : FUnknown(thisPtr), CClass {
    override val ptr: CPointer<IPlugView> get() = thisRawPtr.reinterpret()

    fun isPlatformTypeSupported(type: String): Boolean {
        return IPlugView_isPlatformTypeSupported(this.ptr, type) == kResultTrue
    }

    fun attached(parent: PlatformView, type: String) {
        val result = IPlugView_attached(this.ptr, parent.ptr, type)
        check(result == kResultTrue) { result.kResultString }
    }

    val removed: Boolean get() = IPlugView_removed(this.ptr) == kResultTrue
    fun onWheel(distance: Float): Boolean = IPlugView_onWheel(this.ptr, distance) == kResultTrue

    @ExperimentalUnsignedTypes
	fun onKeyDown(key: Short, keyCode: Short, modifiers: Short): Boolean {
        return Adapter.IPlugView.onKeyDown(this.ptr, key, keyCode, modifiers) == kResultTrue
    }

    @ExperimentalUnsignedTypes
	fun onKeyUp(key: Short, keyCode: Short, modifiers: Short): Boolean {
        return Adapter.IPlugView.onKeyUp(this.ptr, key, keyCode, modifiers) == kResultTrue
    }

    val size: ViewRect
        get() = memScoped {
            val rect = alloc<cwrapper.ViewRect>()
            val result = IPlugView_getSize(this@PlugView.ptr, rect.ptr)
			check(result == kResultTrue) { result.kResultString }
            rect.toKViewRect()
        }

    fun onSize(newSize: ViewRect) {
        memScoped {
            val rect = alloc<cwrapper.ViewRect>()
            rect.set(newSize)
            val result = IPlugView_onSize(this@PlugView.ptr, rect.ptr)
            check(result == kResultTrue) { result.kResultString }
        }
    }

    @OptIn(ExperimentalUnsignedTypes::class)
    fun onFocus(state: Boolean) {
        val result = IPlugView_onFocus(this.ptr, state.toByte().toUByte())
        check(result == kResultTrue) { result.kResultString }
    }

    fun setFrame(frame: PlugFrame) {
        val result = IPlugView_setFrame(this.ptr, frame.ptr)
        check(result == kResultTrue) { result.kResultString }
    }

    val canResize: Boolean
        get() = IPlugView_canResize(this.ptr) == kResultTrue

    fun checkSizeConstraint(rect: ViewRect): Boolean {
        val result = memScoped {
            val buf = alloc<cwrapper.ViewRect>()
            buf.set(rect)
            IPlugView_checkSizeConstraint(this@PlugView.ptr, buf.ptr)
        }
        return result == kResultTrue
    }

}

class PlugFrame(thisPtr: CPointer<IPlugFrame>) : FUnknown(thisPtr), CClass {
    override val ptr: CPointer<IPlugFrame> get() = thisRawPtr.reinterpret()

    fun resizeView(view: PlugView, newSize: ViewRect) {
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