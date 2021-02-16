package io.github.durun.vst3kotlin.vst

import cwrapper.FUnknown_release
import cwrapper.IAudioProcessor
import io.github.durun.vst3kotlin.base.FUnknown
import kotlinx.cinterop.CPointer
import kotlinx.cinterop.reinterpret

actual class AudioProcessor(
	private val thisPtr: CPointer<IAudioProcessor>
) : FUnknown {
	actual override var isOpen: Boolean = true
		private set

	actual override fun close() {
		check(isOpen)
		FUnknown_release(thisPtr.reinterpret())
		isOpen = false
	}
}