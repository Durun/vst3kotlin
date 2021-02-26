package io.github.durun.vst3kotlin.vst

import cwrapper.*
import io.github.durun.vst3kotlin.base.*
import io.github.durun.vst3kotlin.gui.PlugView
import kotlinx.cinterop.*

class Component(
    override val ptr: CPointer<IComponent>
) : PluginBase() {

    val controllerClassID: UID by lazy {
        memScoped {
            val tuid = allocArray<ByteVar>(16)
            val result = IComponent_getControllerClassId(ptr, tuid)
            check(result == kResultTrue) { result.kResultString }
            tuid.toUID()
        }
    }
    val audioInputBusInfos: List<BusInfo> get() = getBusInfos(MediaType.Audio, BusDirection.Input)
    val eventInputBusInfos: List<BusInfo> get() = getBusInfos(MediaType.Event, BusDirection.Input)
    val audioOutputBusInfos: List<BusInfo> get() = getBusInfos(MediaType.Audio, BusDirection.Output)
    val eventOutputBusInfos: List<BusInfo> get() = getBusInfos(MediaType.Event, BusDirection.Output)
    val routingInfo: Pair<RoutingInfo?, RoutingInfo?>
        get() = memScoped {
            val inInfo = alloc<cwrapper.RoutingInfo>()
            val outInfo = alloc<cwrapper.RoutingInfo>()
            val result = IComponent_getRoutingInfo(ptr, inInfo.ptr, outInfo.ptr)
            if (result == kNotImplemented) return null to null
            check(result == kResultTrue) { result.kResultString }
            inInfo.toKRoutingInfo() to outInfo.toKRoutingInfo()
        }
    val state: BStream
        get() {
            val streamPtr = memScoped {
                val stream = alloc<IBStream>()
                val result = IComponent_getState(ptr, stream.ptr)
                check(result == kResultTrue) { result.kResultString }
                stream.ptr
            }
            return BStream(streamPtr)
        }

    fun setState(state: BStream) {
        val result = IComponent_setState(ptr, state.ptr)
        check(result == kResultTrue) { result.kResultString }
    }

    fun setIoMode(mode: IoMode) {
        val result = IComponent_setIoMode(ptr, mode.value)
        check(result == kResultTrue) {
            if (result == kNotImplemented) "$mode is not implemented."
            else result.kResultString
        }
    }

    @kotlin.ExperimentalUnsignedTypes
    fun activate(bus: BusInfo, state: Boolean) {
        val result =
            IComponent_activateBus(ptr, bus.mediaType.value, bus.direction.value, bus.index, state.toByte().toUByte())
        check(result == kResultTrue) { result.kResultString }
    }

    @kotlin.ExperimentalUnsignedTypes
    fun setActive(state: Boolean) {
        val result = IComponent_setActive(ptr, state.toByte().toUByte())
        check(result == kResultTrue) { result.kResultString }
    }

    @kotlin.ExperimentalUnsignedTypes
    fun getBusInfos(
        type: MediaType,
        direction: BusDirection
    ): List<BusInfo> {
        val size = IComponent_getBusCount(ptr, type.value, direction.value)
        return memScoped {
            val infos = allocArray<cwrapper.BusInfo>(size)
            val indice = 0 until size
            indice.forEach { i ->
                val result = IComponent_getBusInfo(ptr, type.value, direction.value, i, infos[i].ptr)
                check(result == kResultTrue) { result.kResultString }
            }
            indice.map { infos[it].toKBusInfo(it) }.toList()
        }
    }

    private fun cwrapper.RoutingInfo.toKRoutingInfo(): RoutingInfo {
        return RoutingInfo(mediaType.toMediaType(), busIndex, channel)
    }

    @kotlin.ExperimentalUnsignedTypes
    private fun cwrapper.BusInfo.toKBusInfo(index: Int): BusInfo {
        return BusInfo(
            index = index,
            mediaType = mediaType.toMediaType(),
            direction = when (direction) {
                BusDirection.Input.value -> BusDirection.Input
                BusDirection.Output.value -> BusDirection.Output
                else -> throw IllegalArgumentException()
            },
            channelCount = channelCount,
            name = name.toKString(),
            busType = when (busType) {
                BusType.Main.value -> BusType.Main
                BusType.Aux.value -> BusType.Aux
                else -> throw IllegalArgumentException()
            },
            defaultActive = flags and kDefaultActive != 0u,
            isControlVoltage = flags and kIsControlVoltage != 0u
        )
    }

    private fun Int.toMediaType(): MediaType = when (this) {
        MediaType.Audio.value -> MediaType.Audio
        MediaType.Event.value -> MediaType.Event
        MediaType.NumMediaTypes.value -> MediaType.NumMediaTypes
        else -> throw IllegalArgumentException()
    }

    fun queryEditController(): EditController {
        return EditController(queryInterface(IEditController_iid).reinterpret())
    }

    fun queryPlugView(): PlugView {
        return PlugView(queryInterface(IPlugView_iid).reinterpret())
    }
}