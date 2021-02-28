package io.github.durun.vst3kotlin.vst

import cwrapper.*

@Deprecated("experimental")
data class ProcessContext(
    val state: Flags,
    val sampleRate: SampleRate,
    val projectTimeSamples: TSamples,
    val systemTime: Long,
    val continousTimeSamples: TSamples,
    val projectTimeMusic: TQuarterNotes,
    val barPositionMusic: TQuarterNotes,
    val cycleStartMusic: TQuarterNotes,
    val cycleEndMusic: TQuarterNotes,
    val tempo: Double,
    val timeSigNumerator: Int,
    val timeSigDenominator: Int,
    val chord: Chord,
    val smpteOffsetSubframes: Int,
    val frameRate: FrameRate,
    val samplesToNextClock: Int
) {
    data class Flags(
        val playing: Boolean,
        val cycleActive: Boolean,
        val recording: Boolean,
        val systemTimeValid: Boolean,
        val contTimeValid: Boolean,
        val projectTimeMusicValid: Boolean,
        val barPositionValid: Boolean,
        val cycleValid: Boolean,
        val tempoValid: Boolean,
        val timeSigValid: Boolean,
        val chordValid: Boolean,
        val smpteValid: Boolean,
        val clockValid: Boolean
    )

    data class Chord(
        val keyNote: Byte,
        val rootNote: Byte,
        val chordMask: Short
    )

    data class FrameRate(
        val framesPerSecond: Int,
        val pullDownRate: Boolean,
        val dropRate: Boolean
    )
}