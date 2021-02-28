package io.github.durun.vst3kotlin.vst

import cwrapper.AudioBusBuffers
import cwrapper.Sample32Var
import io.github.durun.io.Closeable
import io.github.durun.vst3kotlin.cppinterface.CClass
import kotlinx.cinterop.*

abstract class AudioBusBuffer<T : CPointed>(
	val length: Int,
	val numChannels: Int,
	protected val placement: NativeFreeablePlacement
) : Closeable, CClass {
	abstract val channels: CArrayPointer<CArrayPointerVar<T>>
	abstract override val ptr: CPointer<AudioBusBuffers>
	final override var isOpen: Boolean = true
		private set

	override fun close() {
		check(isOpen)
		(0 until numChannels).forEach { i ->
			channels[i]?.let { placement.free(it) }
		}
		placement.free(channels)
		placement.free(ptr)
		isOpen = false
	}
}

class FloatAudioBusBuffer(
	length: Int,
	numChannels: Int,
	placement: NativeFreeablePlacement = nativeHeap
) : AudioBusBuffer<Sample32Var>(length, numChannels, placement) {
	override val channels: CArrayPointer<CArrayPointerVar<Sample32Var>> =
		placement.allocArray<CArrayPointerVar<Sample32Var>>(numChannels)
			.apply {
				(0 until numChannels).forEach { i ->
					this[i] = placement.allocArray(length)
				}
			}
	override val ptr: CPointer<AudioBusBuffers> = placement.alloc<AudioBusBuffers>()
		.also {
			it.numChannels = this.numChannels
			it.silenceFlags = 0u
			it.channelBuffers32 = channels
		}.ptr
}