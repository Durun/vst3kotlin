package io.github.durun.vst3kotlin.vst

import io.github.durun.vst3kotlin.base.FUnknown
import io.github.durun.vstkotlin3.vst.SpeakerArrangement

expect class AudioProcessor : FUnknown {
	fun setBusArrangements(inputs: List<SpeakerArrangement>, outputs: List<SpeakerArrangement>)
	fun getBusArrangement(direction: BusDirection, index: Int): SpeakerArrangement
	val inputBusArrangements: List<SpeakerArrangement>
	val outputBusArrangements: List<SpeakerArrangement>
	fun canProcessSampleSize(sampleSize: SymbolicSampleSize): Boolean
	fun setupProcessing(setup: ProcessSetup)
	fun setProcessing(state: Boolean)
	fun process()// TODO
	val latencySampleSize: Int
	val tailSampleSize: Int
	val processContextRequirement: ProcessContextRequirement
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

data class ProcessContextRequirement(
	val NeedSystemTime: Boolean,            // kSystemTimeValid
	val NeedContinousTimeSamples: Boolean,  // kContTimeValid
	val NeedProjectTimeMusic: Boolean,      // kProjectTimeMusicValid
	val NeedBarPositionMusic: Boolean,      // kBarPositionValid
	val NeedCycleMusic: Boolean,            // kCycleValid
	val NeedSamplesToNextClock: Boolean,    // kClockValid
	val NeedTempo: Boolean,                 // kTempoValid
	val NeedTimeSignature: Boolean,         // kTimeSigValid
	val NeedChord: Boolean,                 // kChordValid
	val NeedFrameRate: Boolean,             // kSmpteValid
	val NeedTransportState: Boolean,        // kPlaying, kCycleActive, kRecording
) {
	@OptIn(ExperimentalUnsignedTypes::class)
	constructor(flags: UInt) : this(
		NeedSystemTime = flags and (1u shl 0) != 0u,
		NeedContinousTimeSamples = flags and (1u shl 1) != 0u,
		NeedProjectTimeMusic = flags and (1u shl 2) != 0u,
		NeedBarPositionMusic = flags and (1u shl 3) != 0u,
		NeedCycleMusic = flags and (1u shl 4) != 0u,
		NeedSamplesToNextClock = flags and (1u shl 5) != 0u,
		NeedTempo = flags and (1u shl 6) != 0u,
		NeedTimeSignature = flags and (1u shl 7) != 0u,
		NeedChord = flags and (1u shl 8) != 0u,
		NeedFrameRate = flags and (1u shl 9) != 0u,
		NeedTransportState = flags and (1u shl 10) != 0u
	)
}