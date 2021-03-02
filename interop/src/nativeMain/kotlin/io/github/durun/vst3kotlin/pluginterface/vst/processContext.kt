package io.github.durun.vst3kotlin.pluginterface.vst

import cwrapper.*

class Fraction(val numerator: Int, val denominator: Int)

infix fun Int.over(denominator: Int): Fraction = Fraction(this, denominator)

data class Chord(
	val root: Byte,     // TODO: Parse chord bitset
	val key: Byte,
	val chord: Short
) {
	@kotlin.ExperimentalUnsignedTypes
	fun writeTo(struct: cwrapper.Chord) {
		struct.apply {
			rootNote = root.toUByte()
			keyNote = key.toUByte()
			chordMask = chord
		}
	}
}

data class FrameRate(
	val fps: Int,
	val pullDownRate: Boolean,
	val dropRate: Boolean
) {
	@kotlin.ExperimentalUnsignedTypes
	fun writeTo(struct: cwrapper.FrameRate) {
		struct.apply {
			framesPerSecond = fps.toUInt()
			flags = (if (pullDownRate) 0b01u else 0u) or
					(if (dropRate) 0b10u else 0u)
		}
	}
}

@kotlin.ExperimentalUnsignedTypes
fun ProcessContext.processContextOf(
	playing: Boolean = false,
	cycleActive: Boolean = false,
	recording: Boolean = false,
	sampleRate: Double,
	projectTimeSamples: TSamples,
	systemTime: Long? = null,
	continousTimeSamples: TSamples? = null,
	tempo: Double? = null,
	projectTimeMusic: TQuarterNotes? = tempo?.let { projectTimeSamples / sampleRate * (it / 60) },
	barPositionMusic: TQuarterNotes? = null,
	cycleStartEndMusic: Pair<TQuarterNotes, TQuarterNotes>? = null,
	timeSig: Fraction? = null,
	chord: Chord? = null,
	smpteOffsetSubframes: Int? = null,
	frameRate: FrameRate? = null,
	samplesToNextClock: Int? = null
): ProcessContext = this.apply {
	var flags: UInt = 0u
	if (playing) flags = flags or kPlaying
	if (cycleActive) flags = flags or kCycleActive
	if (recording) flags = flags or kRecording
	this.sampleRate = sampleRate
	this.projectTimeSamples = projectTimeSamples
	systemTime?.let {
		this.systemTime = it
		flags = flags or kSystemTimeValid
	}
	continousTimeSamples?.let {
		this.continousTimeSamples = it
		flags = flags or kContTimeValid
	}
	projectTimeMusic?.let {
		this.projectTimeMusic = it
		flags = flags or kProjectTimeMusicValid
	}
	barPositionMusic?.let {
		this.barPositionMusic = it
		flags = flags or kBarPositionValid
	}
	cycleStartEndMusic?.let { (start, end) ->
		this.cycleStartMusic = start
		this.cycleEndMusic = end
		flags = flags or kCycleValid
	}
	tempo?.let {
		this.tempo = tempo
		flags = flags or kTempoValid
	}
	timeSig?.let {
		this.timeSigNumerator = it.numerator
		this.timeSigDenominator = it.denominator
		flags = flags or kTimeSigValid
	}
	chord?.let {
		it.writeTo(this.chord)
		flags = flags or kChordValid
	}
	frameRate?.let {
		it.writeTo(this.frameRate)
		smpteOffsetSubframes?.let {
			this.smpteOffsetSubframes = it
			flags = flags or kSmpteValid
		}
	}
	samplesToNextClock?.let {
		this.samplesToNextClock = it
		flags = flags or kClockValid
	}
}