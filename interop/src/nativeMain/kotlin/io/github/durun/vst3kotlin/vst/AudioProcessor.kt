package io.github.durun.vst3kotlin.vst

import cwrapper.*
import io.github.durun.vst3kotlin.base.FUnknown
import io.github.durun.vst3kotlin.base.kResultString
import io.github.durun.vst3kotlin.pluginterface.vst.*
import io.github.durun.vst3kotlin.pluginterface.vst.BusDirection
import io.github.durun.vst3kotlin.pluginterface.vst.MediaType
import io.github.durun.vst3kotlin.pluginterface.vst.ProcessSetup
import io.github.durun.vst3kotlin.pluginterface.vst.SpeakerArrangement
import kotlinx.cinterop.*

class AudioProcessor(
    override val ptr: CPointer<IAudioProcessor>
) : FUnknown() {

    @ExperimentalUnsignedTypes
    fun setBusArrangements(
        inputs: List<SpeakerArrangement>,
        outputs: List<SpeakerArrangement>
    ) {
        memScoped {
            val inArr = allocArray<SpeakerArrangementVar>(inputs.size)
            val outArr = allocArray<SpeakerArrangementVar>(outputs.size)
            inputs.forEachIndexed { i, it -> inArr[i] = it.value }
            outputs.forEachIndexed { i, it -> outArr[i] = it.value }
            val result = IAudioProcessor_setBusArrangements(ptr, inArr, inputs.size, outArr, outputs.size)
            check(result == kResultTrue) { result.kResultString }
        }
    }

    @ExperimentalUnsignedTypes
    private fun getBusArrangement(
        direction: BusDirection,
        index: Int
    ): SpeakerArrangement {
        return memScoped {
            val buf = alloc<SpeakerArrangementVar>()
            val result = IAudioProcessor_getBusArrangement(ptr, direction.value, index, buf.ptr)
            check(result == kResultTrue) { result.kResultString }
            SpeakerArrangement.of(buf.value)
        }
    }

    @ExperimentalUnsignedTypes
    val inputBusArrangements: List<SpeakerArrangement>
        get() {
            val size = queryVstInterface<IComponent>(IComponent_iid).usePointer {
                IComponent_getBusCount(it, MediaType.Audio.value, BusDirection.Input.value)
            }
            return (0 until size).map { i ->
                getBusArrangement(BusDirection.Input, i)
            }
        }

    @ExperimentalUnsignedTypes
    val outputBusArrangements: List<SpeakerArrangement>
        get() {
            val size = queryVstInterface<IComponent>(IComponent_iid).usePointer {
                IComponent_getBusCount(it, MediaType.Audio.value, BusDirection.Output.value)
            }
            return (0 until size).map { i ->
                getBusArrangement(BusDirection.Output, i)
            }
        }

    fun canProcessSampleSize(sampleSize: SymbolicSampleSize): Boolean {
        return IAudioProcessor_canProcessSampleSize(ptr, sampleSize.value) == kResultTrue
    }

    fun setupProcessing(setup: ProcessSetup) {
        memScoped {
            val buf = cValue<cwrapper.ProcessSetup> {
                maxSamplesPerBlock = setup.maxSamplesPerBlock
                processMode = setup.processMode.value
                sampleRate = setup.sampleRate
                symbolicSampleSize = setup.sampleSize.value
            }
            val result = IAudioProcessor_setupProcessing(ptr, buf.ptr)
            check(result == kResultTrue) { result.kResultString }
        }
    }

    @ExperimentalUnsignedTypes
    fun setProcessing(state: Boolean) {
        IAudioProcessor_setProcessing(ptr, state.toByte().toUByte())
    }

    fun process(data: ProcessData) {
        val result = IAudioProcessor_process(ptr, data.ptr)
        check(result == kResultOk) { result.kResultString }
    }

    @ExperimentalUnsignedTypes
    val latencySampleSize: Int
        get() = IAudioProcessor_getLatencySamples(ptr).toInt()

    @ExperimentalUnsignedTypes
    val tailSampleSize: Int
        get() = IAudioProcessor_getTailSamples(ptr).toInt()

    @ExperimentalUnsignedTypes
    val processContextRequirement: ProcessContextRequirement
        get() {
            val flags = queryVstInterface<IProcessContextRequirements>(IProcessContextRequirements_iid).usePointer {
                IProcessContextRequirements_getProcessContextRequirements(it)
            }
            return ProcessContextRequirement(flags)
        }
}