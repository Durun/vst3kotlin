package io.github.durun.vst3kotlin.vst

import cwrapper.*
import io.github.durun.vst3kotlin.cppinterface.CClass
import io.github.durun.vst3kotlin.VstInterface
import io.github.durun.vst3kotlin.base.BStream
import io.github.durun.vst3kotlin.base.PluginBase
import io.github.durun.vst3kotlin.base.kResultString
import io.github.durun.vst3kotlin.gui.PlugView
import kotlinx.cinterop.*

class EditController(
    override val ptr: CPointer<IEditController>
) : PluginBase() {
    private val this2: VstInterface<CPointer<IEditController2>> = queryVstInterface(IEditController2_iid)
    private val thisPtr2: CPointer<IEditController2> get() = this2.ptr.reinterpret()

    fun setComponentState(state: BStream) {
        val result = IEditController_setComponentState(ptr, state.ptr)
        check(result == kResultTrue) { result.kResultString }
    }

    fun setState(state: BStream) {
        val result = IEditController_setState(ptr, state.ptr)
        check(result == kResultTrue) { result.kResultString }
    }

    val state: BStream
        get() = memScoped {
            val buf = alloc<IBStream>()
            val result = IEditController_getState(ptr, buf.ptr)
            check(result == kResultTrue) { result.kResultString }
            BStream(buf.ptr)
        }

    @kotlin.ExperimentalUnsignedTypes
    val parameterInfo: List<ParameterInfo>
        get() = memScoped {
            val size = IEditController_getParameterCount(ptr)
            val infos = allocArray<cwrapper.ParameterInfo>(size)
            (0 until size).forEach {
                val result = IEditController_getParameterInfo(ptr, it, infos[it].ptr)
                check(result == kResultTrue) { result.kResultString }
            }
            (0 until size).map {
                infos[it].toKParameterInfo()
            }
        }

    @kotlin.ExperimentalUnsignedTypes
    fun getParamStringByValue(id: UInt, valueNormalized: Double): String {
        return memScoped {
            val buf = alloc<String128Var>()
            val str = buf.value
            val result = IEditController_getParamStringByValue(ptr, id, valueNormalized, str)
            check(result == kResultTrue) { result.kResultString }
            checkNotNull(str)
            str.toKString()
        }
    }

    @kotlin.ExperimentalUnsignedTypes
    fun getDoubleByString(id: UInt, string: String): Double {
        return memScoped {
            val buf = alloc<DoubleVar>()
            val strPtr = string.cstr.ptr.reinterpret<TCharVar>()
            val result = IEditController_getParamValueByString(ptr, id, strPtr, buf.ptr)
            check(result == kResultTrue) { result.kResultString }
            buf.value
        }
    }

    @kotlin.ExperimentalUnsignedTypes
    fun normalizedParamToPlain(id: UInt, valueNormalized: Double): Double {
        return IEditController_normalizedParamToPlain(ptr, id, valueNormalized)
    }

    @kotlin.ExperimentalUnsignedTypes
    fun plainParamToNormalized(id: UInt, plainValue: Double): Double {
        return IEditController_plainParamToNormalized(ptr, id, plainValue)
    }

    @kotlin.ExperimentalUnsignedTypes
    fun getParamNormalized(id: UInt): Double {
        return IEditController_getParamNormalized(ptr, id)
    }

    @kotlin.ExperimentalUnsignedTypes
    fun setParamNormalized(id: UInt, value: Double) {
        val result = IEditController_setParamNormalized(ptr, id, value)
        check(result == kResultTrue) { result.kResultString }
    }

    fun setComponentHandler(handler: ComponentHandler) {
        val result = IEditController_setComponentHandler(ptr, handler.ptr)
        check(result == kResultTrue) { result.kResultString }
    }

    fun createView(name: String): PlugView {
        val ptr = IEditController_createView(ptr, name)
        checkNotNull(ptr)
        return PlugView(ptr)
    }

    @kotlin.ExperimentalUnsignedTypes
    fun setKnobMode(mode: KnobMode) {
        IEditController2_setKnobMode(thisPtr2, mode.value.toUInt())
    }

    @kotlin.ExperimentalUnsignedTypes
    fun openHelp(onlyCheck: Boolean) {
        val result = IEditController2_openHelp(thisPtr2, onlyCheck.toByte().toUByte())
        check(result == kResultTrue) { result.kResultString }
    }

    @kotlin.ExperimentalUnsignedTypes
    fun openAboutBox(onlyCheck: Boolean) {
        val result = IEditController2_openAboutBox(thisPtr2, onlyCheck.toByte().toUByte())
        check(result == kResultTrue) { result.kResultString }
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