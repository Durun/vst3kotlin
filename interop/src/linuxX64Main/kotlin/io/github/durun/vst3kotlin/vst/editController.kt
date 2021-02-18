package io.github.durun.vst3kotlin.vst

import cwrapper.*
import io.github.durun.vst3kotlin.VstInterface
import io.github.durun.vst3kotlin.base.BStream
import io.github.durun.vst3kotlin.base.FUnknown
import io.github.durun.vst3kotlin.base.PluginBase
import io.github.durun.vst3kotlin.base.kResultString
import io.github.durun.vst3kotlin.gui.PlugView
import kotlinx.cinterop.*

actual class ComponentHandler(thisPtr: CPointer<IComponentHandler>) : FUnknown(thisPtr) {
	val ptr: CPointer<IComponentHandler> get() = thisPtr
	private val thisPtr: CPointer<IComponentHandler> get() = thisRawPtr.reinterpret()
	private val this2: VstInterface<CPointer<IComponentHandler2>> = VstInterface(queryInterface(IComponentHandler2_iid).reinterpret())
	private val thisPtr2: CPointer<IComponentHandler2> = this2.ptr.reinterpret()

	override fun close() {
		this2.close()
		super.close()
	}

	actual fun beginEdit(id: UInt) {
		val result = IComponentHandler_beginEdit(thisPtr, id)
		check(result == kResultTrue) { result.kResultString }
	}

	actual fun performEdit(id: UInt, valueNormalized: Double) {
		val result = IComponentHandler_performEdit(thisPtr, id, valueNormalized)
		check(result == kResultTrue) { result.kResultString }
	}

	actual fun endEdit(id: UInt) {
		val result = IComponentHandler_endEdit(thisPtr, id)
		check(result == kResultTrue) { result.kResultString }
	}

	actual fun restartComponent(flags: Int) {
		val result = IComponentHandler_restartComponent(thisPtr, flags)
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

actual class EditController(thisPtr: CPointer<IEditController>) : PluginBase(thisPtr) {
	private val thisPtr: CPointer<IEditController> get() = thisRawPtr.reinterpret()
	private val this2: VstInterface<CPointer<IEditController2>> = queryInterface()
	private val thisPtr2: CPointer<IEditController2> get() = this2.ptr.reinterpret()

	actual fun setComponentState(state: BStream) {
		val result = IEditController_setComponentState(thisPtr, state.ptr)
		check(result == kResultTrue) { result.kResultString }
	}

	actual fun setState(state: BStream) {
		val result = IEditController_setState(thisPtr, state.ptr)
		check(result == kResultTrue) { result.kResultString }
	}

	actual val state: BStream
		get() = memScoped {
			val buf = alloc<IBStream>()
			val result = IEditController_getState(thisPtr, buf.ptr)
			check(result == kResultTrue) { result.kResultString }
			BStream(buf.ptr)
		}

	actual val parameterInfo: List<ParameterInfo>
		get() = memScoped {
			val size = IEditController_getParameterCount(thisPtr)
			val infos = allocArray<cwrapper.ParameterInfo>(size)
			(0 until size).forEach {
				val result = IEditController_getParameterInfo(thisPtr, it, infos[it].ptr)
				check(result == kResultTrue) { result.kResultString }
			}
			(0 until size).map {
				infos[it].toKParameterInfo()
			}
		}


	actual fun getParamStringByValue(id: UInt, valueNormalized: Double): String {
		return memScoped {
			val buf = alloc<String128Var>()
			val str = buf.value
			val result = IEditController_getParamStringByValue(thisPtr, id, valueNormalized, str)
			check(result == kResultTrue) { result.kResultString }
			checkNotNull(str)
			str.toKString()
		}
	}

	actual fun getDoubleByString(id: UInt, string: String): Double {
		return memScoped {
			val buf = alloc<DoubleVar>()
			val strPtr = string.cstr.ptr.reinterpret<TCharVar>()
			val result = IEditController_getParamValueByString(thisPtr, id, strPtr, buf.ptr)
			check(result == kResultTrue) { result.kResultString }
			buf.value
		}
	}

	actual fun normalizedParamToPlain(id: UInt, valueNormalized: Double): Double {
		return IEditController_normalizedParamToPlain(thisPtr, id, valueNormalized)
	}

	actual fun plainParamToNormalized(id: UInt, plainValue: Double): Double {
		return IEditController_plainParamToNormalized(thisPtr, id, plainValue)
	}

	actual fun getParamNormalized(id: UInt): Double {
		return IEditController_getParamNormalized(thisPtr, id)
	}

	actual fun setParamNormalized(id: UInt, value: Double) {
		val result = IEditController_setParamNormalized(thisPtr, id, value)
		check(result == kResultTrue) { result.kResultString }
	}

	actual fun setComponentHandler(handler: ComponentHandler) {
		val result = IEditController_setComponentHandler(thisPtr, handler.ptr)
		check(result == kResultTrue) { result.kResultString }
	}

	actual fun createView(name: String): PlugView {
		val ptr = IEditController_createView(thisPtr, name)
		checkNotNull(ptr)
		return PlugView(ptr)
	}
}

fun cwrapper.ParameterInfo.toKParameterInfo(): ParameterInfo {
	return ParameterInfo(
		id,
		title.toKString(),
		shortTitle.toKString(),
		units.toKString(),
		stepCount, defaultNormalizedValue, unitId, flags
	)
}