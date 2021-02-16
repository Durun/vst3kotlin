package io.github.durun.vst3kotlin.vst

import cwrapper.IAudioProcessor
import io.github.durun.vst3kotlin.base.FUnknown
import kotlinx.cinterop.CPointer

actual class AudioProcessor(
	thisPtr: CPointer<IAudioProcessor>
) : FUnknown(thisPtr) {
}