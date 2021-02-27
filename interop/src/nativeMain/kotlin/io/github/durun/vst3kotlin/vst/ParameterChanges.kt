package io.github.durun.vst3kotlin.vst

import cwrapper.*
import io.github.durun.vst3kotlin.base.FUnknown
import io.github.durun.vst3kotlin.base.kResultString
import kotlinx.cinterop.*


class ParameterValueQueue(
    override val ptr: CPointer<IParamValueQueue>
) : FUnknown() {
    data class Point(val sampleOffset: Int, val value: ParamValue)

    val parameterID: ParamID
        get() = IParamValueQueue_getParameterId(ptr)

    val points = PointProperty()

    inner class PointProperty {
        val size: Int
            get() = IParamValueQueue_getPointCount(ptr)

        operator fun get(index: Int): Point = memScoped {
            val offset = alloc<IntVar>()
            val value = alloc<ParamValueVar>()
            val result = IParamValueQueue_getPoint(ptr, index, offset.ptr, value.ptr)
            check(result == kResultOk) { result.kResultString }
            Point(offset.value, value.value)
        }

        // returns index of added point
        fun add(point: Point): Int = memScoped {
            val index = alloc<IntVar>()
            val result = IParamValueQueue_addPoint(ptr, point.sampleOffset, point.value, index.ptr)
            check(result == kResultOk) { result.kResultString }
            index.value
        }
    }
}

class ParameterChanges(
    override val ptr: CPointer<IParameterChanges>
) : FUnknown() {
    val size: Int
        get() = IParameterChanges_getParameterCount(ptr)

    private val openInterfaces: MutableMap<Int, ParameterValueQueue> = mutableMapOf()

    operator fun get(index: Int): ParameterValueQueue? {
        return openInterfaces[index]
            ?: getRegistering(index)
    }

    // returns index of added point
    @ExperimentalUnsignedTypes
    fun add(id: Int): Int = add(id.toUInt())
    fun add(id: ParamID): Int = memScoped {
        val index = alloc<IntVar>()
        val idPtr = alloc<ParamIDVar>().apply { value = id }.ptr
        val data = IParameterChanges_addParameterData(ptr, idPtr, index.ptr)
        checkNotNull(data)
        val boxedData = ParameterValueQueue(data)
        register(index.value, boxedData)
        index.value
    }

    override fun close() {
        //openInterfaces.values.forEach { it.close() }
        super.close()
    }

    private fun register(index: Int, data: ParameterValueQueue) {
        require(openInterfaces[index] == null) { "Internal error. Fix ${this::class}" }
        openInterfaces[index] = data
    }

    private fun getRegistering(index: Int): ParameterValueQueue? {
        return IParameterChanges_getParameterData(ptr, index)
            ?.let { ParameterValueQueue(it) }
            ?.also { register(index, it) }
    }
}