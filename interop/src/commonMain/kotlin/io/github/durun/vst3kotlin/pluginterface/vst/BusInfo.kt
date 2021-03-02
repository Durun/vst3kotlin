package io.github.durun.vst3kotlin.pluginterface.vst

enum class IoMode(val value: Int) {
    Simple(0), Advanced(1), OfflineProcessing(2)
}

enum class MediaType(val value: Int) {
    Audio(0), Event(1), NumMediaTypes(2)
}

enum class BusDirection(val value: Int) {
    Input(0), Output(1)
}

enum class BusType(val value: Int) {
    Main(0), Aux(1)
}

data class BusInfo(
	val index: Int,
	val mediaType: MediaType,
	val direction: BusDirection,
	val channelCount: Int,
	val name: String,
	val busType: BusType,
	val defaultActive: Boolean,
	val isControlVoltage: Boolean
)

data class RoutingInfo(
	val mediaType: MediaType,
	val busIndex: Int,
	val channel: Int
)