package io.github.durun.vst3kotlin.vst

import io.github.durun.vst3kotlin.base.FUnknown
import io.github.durun.vstkotlin3.vst.SpeakerArrangement

expect class AudioProcessor : FUnknown {
	fun setBusArrangements(inputs: List<SpeakerArrangement>, outputs: List<SpeakerArrangement>)
	fun getBusArrangement(direction: BusDirection, index: Int): SpeakerArrangement
	fun canProcessSampleSize(sampleSize: SymbolicSampleSize): Boolean
	fun setupProcessing(setup: ProcessSetup)
	fun setProcessing(state: Boolean)
	fun process()// TODO
	val latencySampleSize: Int
	val tailSampleSize: Int
}

class ComponentFlag
@OptIn(ExperimentalUnsignedTypes::class)
constructor(val value: UInt) {
	@OptIn(ExperimentalUnsignedTypes::class)
	val distributable: Boolean = value and 1u != 0u

	@OptIn(ExperimentalUnsignedTypes::class)
	val simpleModeSupported: Boolean = value and (1u shl 1) != 0u
}

enum class SymbolicSampleSize(val value: Int) {
	Sample32(0), Sample64(1)
}

enum class ProcessMode(val value: Int) {
	Realtime(0), Prefetch(1), Offline(2)
}

data class ProcessSetup(
	val processMode: Int,
	val symbolicSampleSize: Int,
	val maxSamplesPerBlock: Int,
	val sampleRate: Double
)
