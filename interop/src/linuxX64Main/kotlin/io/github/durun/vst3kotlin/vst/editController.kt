package io.github.durun.vst3kotlin.vst

import cwrapper.*
import io.github.durun.vst3kotlin.VstInterface
import io.github.durun.vst3kotlin.base.BStream
import io.github.durun.vst3kotlin.base.PluginBase
import io.github.durun.vst3kotlin.base.kResultString
import io.github.durun.vst3kotlin.gui.PlugView
import kotlinx.cinterop.*


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

	@kotlin.ExperimentalUnsignedTypes
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

	@kotlin.ExperimentalUnsignedTypes
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

	@kotlin.ExperimentalUnsignedTypes
	actual fun getDoubleByString(id: UInt, string: String): Double {
		return memScoped {
			val buf = alloc<DoubleVar>()
			val strPtr = string.cstr.ptr.reinterpret<TCharVar>()
			val result = IEditController_getParamValueByString(thisPtr, id, strPtr, buf.ptr)
			check(result == kResultTrue) { result.kResultString }
			buf.value
		}
	}

	@kotlin.ExperimentalUnsignedTypes
	actual fun normalizedParamToPlain(id: UInt, valueNormalized: Double): Double {
		return IEditController_normalizedParamToPlain(thisPtr, id, valueNormalized)
	}

	@kotlin.ExperimentalUnsignedTypes
	actual fun plainParamToNormalized(id: UInt, plainValue: Double): Double {
		return IEditController_plainParamToNormalized(thisPtr, id, plainValue)
	}

	@kotlin.ExperimentalUnsignedTypes
	actual fun getParamNormalized(id: UInt): Double {
		return IEditController_getParamNormalized(thisPtr, id)
	}

	@kotlin.ExperimentalUnsignedTypes
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

@kotlin.ExperimentalUnsignedTypes
fun cwrapper.ParameterInfo.toKParameterInfo(): ParameterInfo {
	return ParameterInfo(
		id,
		title.toKString(),
		shortTitle.toKString(),
		units.toKString(),
		stepCount, defaultNormalizedValue, unitId, flags
	)
}