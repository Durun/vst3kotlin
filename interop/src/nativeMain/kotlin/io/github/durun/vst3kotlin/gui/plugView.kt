package io.github.durun.vst3kotlin.gui

import cwrapper.*
import io.github.durun.data.IntBox
import io.github.durun.vst3kotlin.Adapter
import io.github.durun.vst3kotlin.base.FUnknown
import io.github.durun.vst3kotlin.base.kResultString
import io.github.durun.vst3kotlin.window.Window
import kotlinx.cinterop.*

class PlatformView(val ptr: COpaquePointer)

enum class ViewType(val value: String) {
    Editor("editor")
}

class PlugView(
    override val ptr: CPointer<IPlugView>
) : FUnknown() {
    fun isPlatformTypeSupported(type: String): Boolean {
        return IPlugView_isPlatformTypeSupported(this.ptr, type) == kResultTrue
    }

    fun attached(window: Window) {
        window.resize(box.size)
        val result = IPlugView_attached(this.ptr, window.ptr, Window.platformType)
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

    val box: IntBox
        get() = memScoped {
            val rect = alloc<cwrapper.ViewRect>()
            val result = IPlugView_getSize(this@PlugView.ptr, rect.ptr)
            check(result == kResultTrue) { result.kResultString }
            rect.toIntBox()
        }

    fun onSize(newSize: IntBox) {
        memScoped {
            val rect = alloc<cwrapper.ViewRect>()
            rect.set(newSize)
            val result = IPlugView_onSize(this@PlugView.ptr, rect.ptr)
            check(result == kResultTrue) { result.kResultString }
        }
    }

    @kotlin.ExperimentalUnsignedTypes
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

    fun checkSizeConstraint(rect: IntBox): Boolean {
        val result = memScoped {
            val buf = alloc<cwrapper.ViewRect>()
            buf.set(rect)
            IPlugView_checkSizeConstraint(this@PlugView.ptr, buf.ptr)
        }
        return result == kResultTrue
    }

}

class PlugFrame(
    override val ptr: CPointer<IPlugFrame>
) : FUnknown() {
    fun resizeView(view: PlugView, newSize: IntBox) {
        memScoped {
            val rect = alloc<cwrapper.ViewRect>()
            rect.set(newSize)
            val result = IPlugFrame_resizeView(this@PlugFrame.ptr, view.ptr, rect.ptr)
            check(result == kResultTrue) { result.kResultString }
        }
    }
}

private fun cwrapper.ViewRect.toIntBox(): IntBox {
    return IntBox(left, top, right, bottom)
}

private fun cwrapper.ViewRect.set(value: IntBox) {
    left = value.left
    top = value.top
    right = value.right
    bottom = value.bottom
}