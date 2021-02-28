package io.github.durun.vst3kotlin.vst

import cwrapper.AudioBusBuffers
import cwrapper.IEventList
import cwrapper.IParameterChanges
import cwrapper.ProcessContext
import cwrapper.ProcessData
import kotlinx.cinterop.*

@Deprecated("experimental")
data class AudioBusBuffer(
    val numChannels: Int,
    val silenceFlags: Long,
    val channelBuffers32: CArrayPointer<CArrayPointerVar<FloatVar>>?,
    val channelBuffers64: CArrayPointer<CArrayPointerVar<DoubleVar>>?
)

@Deprecated("experimental")
data class ProcessData(
    val processMode: ProcessMode,
    val symbolicSampleSize: SymbolicSampleSize,
    val numSamples: Int,
    val numInputs: Int,
    val numOutputs: Int,
    val inputs: AudioBusBuffer,
    val outputs: AudioBusBuffer,
    val inputParameterChanges: IParameterChanges,
    val outputParameterChanges: IParameterChanges,
    val inputEvents: IEventList,
    val outputEvents: IEventList,
    val processContext: ProcessContext
)

fun processDataOf(): CValue<cwrapper.ProcessData> = cValue<cwrapper.ProcessData> {

}