package io.github.durun.vst3kotlin.vst

import cwrapper.AudioBusBuffers
import cwrapper.IEventList
import cwrapper.IParameterChanges
import cwrapper.ProcessContext
import kotlinx.cinterop.CPointer

fun cwrapper.ProcessData.processDataOf(
    mode: ProcessMode,
    sampleSize: SymbolicSampleSize,
    numSamples: Int,
    numInputs: Int,
    numOutputs: Int,
    inputs: CPointer<AudioBusBuffers>? = null,
    outputs: CPointer<AudioBusBuffers>,
    inputParam: CPointer<IParameterChanges>? = null,
    outputParam: CPointer<IParameterChanges>? = null,
    inputEvent: CPointer<IEventList>? = null,
    outputEvent: CPointer<IEventList>? = null,
    context: CPointer<ProcessContext>
): cwrapper.ProcessData = this.apply {
    this.processMode = mode.value
    this.symbolicSampleSize = sampleSize.value
    this.numSamples = numSamples
    this.numInputs = numInputs
    this.numOutputs = numOutputs
    this.inputs = inputs
    this.outputs = outputs
    this.inputParameterChanges = inputParam
    this.outputParameterChanges = outputParam
    this.inputEvents = inputEvent
    this.outputEvents = outputEvent
    this.processContext = context
}