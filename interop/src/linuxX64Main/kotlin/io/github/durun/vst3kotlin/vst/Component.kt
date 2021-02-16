package io.github.durun.vst3kotlin.vst

import cwrapper.IAudioProcessor
import io.github.durun.vst3kotlin.base.PluginBase
import kotlinx.cinterop.CPointer

actual class Component(
	thisPtr: CPointer<IAudioProcessor>
) : PluginBase(thisPtr) {
}