package io.github.durun.vst3kotlin.hosting

import io.github.durun.io.Closeable
import io.github.durun.vst3kotlin.base.PluginFactory
import io.github.durun.vst3kotlin.base.UID
import io.github.durun.vst3kotlin.cppinterface.CClass
import io.github.durun.vst3kotlin.cppinterface.HostCallback
import io.github.durun.vst3kotlin.vst.AudioProcessor
import io.github.durun.vst3kotlin.vst.Component
import io.github.durun.vst3kotlin.vst.IoMode
import io.github.durun.vst3kotlin.vst.SymbolicSampleSize

class PluginInstance
private constructor(
	val component: Component,
	val processor: AudioProcessor
) : Closeable {
	companion object {
		fun create(from: PluginFactory, classID: UID, hostContext: CClass = HostCallback): PluginInstance {
			val component = from.createComponent(classID)
			component.setIoMode(IoMode.Advanced)
			component.initialize(hostContext)
			val processor = from.createAudioProcessor(classID)
			check(processor.canProcessSampleSize(SymbolicSampleSize.Sample32)) { "AudioProcessor can't process 32bit samples (cid=$classID)" }
			return PluginInstance(component, processor)
		}
	}

	override var isOpen: Boolean = true
		private set

	override fun close() {
		check(isOpen)
		component.close()
		processor.close()
		isOpen = false
	}
}