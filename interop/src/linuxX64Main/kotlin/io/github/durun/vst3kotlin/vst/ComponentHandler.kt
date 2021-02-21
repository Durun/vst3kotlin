package io.github.durun.vst3kotlin.vst

import cwrapper.*
import io.github.durun.util.CClass
import io.github.durun.vst3kotlin.VstInterface
import io.github.durun.vst3kotlin.base.FUnknown
import io.github.durun.vst3kotlin.base.kResultString
import kotlinx.cinterop.CPointer
import kotlinx.cinterop.reinterpret
import kotlinx.cinterop.toByte

actual class ComponentHandler(thisPtr: CPointer<IComponentHandler>) : FUnknown(thisPtr), CClass {
	override val ptr: CPointer<IComponentHandler> get() = thisRawPtr.reinterpret()
	private val this2: VstInterface<CPointer<IComponentHandler2>> =
		VstInterface(queryInterface(IComponentHandler2_iid).reinterpret())
	private val thisPtr2: CPointer<IComponentHandler2> = this2.ptr.reinterpret()

	override fun close() {
		this2.close()
		super.close()
	}

	actual fun beginEdit(id: UInt) {
		val result = IComponentHandler_beginEdit(this.ptr, id)
		check(result == kResultTrue) { result.kResultString }
	}

	actual fun performEdit(id: UInt, valueNormalized: Double) {
		val result = IComponentHandler_performEdit(this.ptr, id, valueNormalized)
		check(result == kResultTrue) { result.kResultString }
	}

	actual fun endEdit(id: UInt) {
		val result = IComponentHandler_endEdit(this.ptr, id)
		check(result == kResultTrue) { result.kResultString }
	}

	actual fun restartComponent(flags: Int) {
		val result = IComponentHandler_restartComponent(this.ptr, flags)
		check(result == kResultTrue) { result.kResultString }
	}

	@OptIn(ExperimentalUnsignedTypes::class)
	actual fun setDirty(state: Boolean) {
		val result = IComponentHandler2_setDirty(thisPtr2, state.toByte().toUByte())
		check(result == kResultTrue) { result.kResultString }
	}

	actual fun requestOpenEditor(name: String) {
		val result = IComponentHandler2_requestOpenEditor(thisPtr2, name)
		check(result == kResultTrue) { result.kResultString }
	}

	actual fun startGroupEdit() {
		val result = IComponentHandler2_startGroupEdit(thisPtr2)
		check(result == kResultTrue) { result.kResultString }
	}

	actual fun finishGroupEdit() {
		val result = IComponentHandler2_finishGroupEdit(thisPtr2)
		check(result == kResultTrue) { result.kResultString }
	}
}