package io.github.durun.vst3kotlin.vst

import cwrapper.*
import io.github.durun.util.CClass
import io.github.durun.vst3kotlin.base.FUnknown
import io.github.durun.vst3kotlin.base.kResultString
import io.github.durun.vstkotlin3.vst.SpeakerArrangement
import kotlinx.cinterop.*

class AudioProcessor(thisPtr: CPointer<IAudioProcessor>) : FUnknown(thisPtr), CClass {
	override val ptr: CPointer<IAudioProcessor> get() = thisRawPtr.reinterpret()
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

	fun getBusArrangement(
		direction: BusDirection,
		index: Int
	): SpeakerArrangement {
		TODO()
		return memScoped {
			val buf = alloc<SpeakerArrangementVar>()
			val result = IAudioProcessor_getBusArrangement(ptr, direction.value, index, buf.ptr)
			check(result == kResultTrue) { result.kResultString }
			SpeakerArrangement.of(buf.value)
		}
	}

	val inputBusArrangements: List<SpeakerArrangement>
		get() {
			val size = queryVstInterface<IComponent>(IComponent_iid).usePointer {
				IComponent_getBusCount(it, MediaType.Audio.value, BusDirection.Input.value)
			}
			return (0 until size).map { i ->
				getBusArrangement(BusDirection.Input, i)
			}
		}
	val outputBusArrangements: List<SpeakerArrangement>
		get() = TODO("Not yet implemented")

	fun canProcessSampleSize(sampleSize: SymbolicSampleSize): Boolean {
		return IAudioProcessor_canProcessSampleSize(ptr, sampleSize.value) == kResultTrue
	}

	fun setupProcessing(setup: ProcessSetup) {
		memScoped {
			val buf = cValue<cwrapper.ProcessSetup> {
				maxSamplesPerBlock = setup.maxSamplesPerBlock
				processMode = setup.processMode
				sampleRate = setup.sampleRate
				symbolicSampleSize = setup.symbolicSampleSize
			}
			val result = IAudioProcessor_setupProcessing(ptr, buf.ptr)
			check(result == kResultTrue) { result.kResultString }
		}
	}

	@OptIn(ExperimentalUnsignedTypes::class)
	fun setProcessing(state: Boolean) {
		IAudioProcessor_setProcessing(ptr, state.toByte().toUByte())
	}

	fun process() {
		TODO()
	}

	@OptIn(ExperimentalUnsignedTypes::class)
	val latencySampleSize: Int
		get() = IAudioProcessor_getLatencySamples(ptr).toInt()

	@OptIn(ExperimentalUnsignedTypes::class)
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