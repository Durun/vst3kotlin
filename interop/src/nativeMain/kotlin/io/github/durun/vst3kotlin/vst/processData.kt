package io.github.durun.vst3kotlin.vst

import cwrapper.IEventList
import cwrapper.IParameterChanges
import cwrapper.ProcessContext
import kotlinx.cinterop.CPointer

fun cwrapper.ProcessData.processDataOf(
    mode: ProcessMode,
    inputAudio: FloatAudioBusBuffer? = null,
    outputAudio: FloatAudioBusBuffer,
    inputParam: CPointer<IParameterChanges>? = null,
    outputParam: CPointer<IParameterChanges>? = null,
    inputEvent: CPointer<IEventList>? = null,
    outputEvent: CPointer<IEventList>? = null,
    context: CPointer<ProcessContext>
): cwrapper.ProcessData = this.apply {
    this.processMode = mode.value
    this.symbolicSampleSize = SymbolicSampleSize.Sample32.value
    this.numSamples = outputAudio.length
    inputAudio?.let {
        check(it.length == numSamples) { "Sample size of all audio buffers must be same" }
        this.numInputs = it.numChannels
        this.inputs = it.ptr
    }
    this.numOutputs = outputAudio.numChannels
    this.outputs = outputAudio.ptr
    this.inputParameterChanges = inputParam
    this.outputParameterChanges = outputParam
    this.inputEvents = inputEvent
    this.outputEvents = outputEvent
    this.processContext = context
}