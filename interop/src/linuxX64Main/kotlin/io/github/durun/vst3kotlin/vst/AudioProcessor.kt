package io.github.durun.vst3kotlin.vst

import cwrapper.*
import io.github.durun.vst3kotlin.base.FUnknown
import io.github.durun.vst3kotlin.base.kResultString
import io.github.durun.vstkotlin3.vst.SpeakerArrangement
import kotlinx.cinterop.*

actual class AudioProcessor(
	thisPtr: CPointer<IAudioProcessor>
) : FUnknown(thisPtr) {
	private val thisPtr: CPointer<IAudioProcessor> get() = thisRawPtr.reinterpret()
	actual fun setBusArrangements(
		inputs: List<SpeakerArrangement>,
		outputs: List<SpeakerArrangement>
	) {
		memScoped {
			val inArr = allocArray<SpeakerArrangementVar>(inputs.size)
			val outArr = allocArray<SpeakerArrangementVar>(outputs.size)
			inputs.forEachIndexed { i, it -> inArr[i] = it.value }
			outputs.forEachIndexed { i, it -> outArr[i] = it.value }
			val result = IAudioProcessor_setBusArrangements(thisPtr, inArr, inputs.size, outArr, outputs.size)
			check(result == kResultTrue) { result.kResultString }
		}
	}

	actual fun getBusArrangement(
		direction: BusDirection,
		index: Int
	): SpeakerArrangement {
		return memScoped {
			val buf = alloc<SpeakerArrangementVar>()
			val result = IAudioProcessor_getBusArrangement(thisPtr, direction.value, index, buf.ptr)
			check(result == kResultTrue) { result.kResultString }
			SpeakerArrangement.of(buf.value)
		}
	}

	actual fun canProcessSampleSize(sampleSize: SymbolicSampleSize): Boolean {
		return IAudioProcessor_canProcessSampleSize(thisPtr, sampleSize.value) == kResultTrue
	}

	actual fun setupProcessing(setup: ProcessSetup) {
		memScoped {
			val buf = cValue<cwrapper.ProcessSetup> {
				maxSamplesPerBlock = setup.maxSamplesPerBlock
				processMode = setup.processMode
				sampleRate = setup.sampleRate
				symbolicSampleSize = setup.symbolicSampleSize
			}
			val result = IAudioProcessor_setupProcessing(thisPtr, buf.ptr)
			check(result == kResultTrue) { result.kResultString }
		}
	}

	@OptIn(ExperimentalUnsignedTypes::class)
	actual fun setProcessing(state: Boolean) {
		IAudioProcessor_setProcessing(thisPtr, state.toByte().toUByte())
	}

	actual fun process() {
		TODO()
	}

	@OptIn(ExperimentalUnsignedTypes::class)
	actual val latencySampleSize: Int
		get() = IAudioProcessor_getLatencySamples(thisPtr).toInt()

	@OptIn(ExperimentalUnsignedTypes::class)
	actual val tailSampleSize: Int
		get() = IAudioProcessor_getTailSamples(thisPtr).toInt()

	actual val processContextRequirement: ProcessContextRequirement
		get() {
			val flags = queryInterface<IProcessContextRequirements>().usePointer {
				IProcessContextRequirements_getProcessContextRequirements(it)
			}
			return ProcessContextRequirement(flags)
		}
}