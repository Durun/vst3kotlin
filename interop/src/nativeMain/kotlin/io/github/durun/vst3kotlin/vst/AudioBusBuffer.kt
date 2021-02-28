package io.github.durun.vst3kotlin.vst

import cwrapper.AudioBusBuffers
import cwrapper.Sample32Var
import io.github.durun.io.Closeable
import io.github.durun.vst3kotlin.cppinterface.CClass
import kotlinx.cinterop.*

class FloatAudioBusBuffer(
	val length: Int,
	val numChannels: Int,
	private val placement: NativeFreeablePlacement = nativeHeap
) : Closeable, CClass {
	val channels: CArrayPointer<CArrayPointerVar<Sample32Var>> =
		placement.allocArray<CArrayPointerVar<Sample32Var>>(numChannels)
			.apply {
				(0 until numChannels).forEach { i ->
					this[i] = placement.allocArray(length)
				}
			}
	override val ptr: CPointer<AudioBusBuffers> = placement.alloc<AudioBusBuffers>()
		.apply {
			this.numChannels = this@FloatAudioBusBuffer.numChannels
			silenceFlags = 0u
			channelBuffers32 = channels
		}.ptr
	override var isOpen: Boolean = true
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