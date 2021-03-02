package io.github.durun.vst3kotlin.hosting

import io.github.durun.resource.Closeable
import io.github.durun.util.logger
import io.github.durun.vst3kotlin.base.PluginFactory
import io.github.durun.vst3kotlin.pluginterface.base.UID
import io.github.durun.vst3kotlin.pluginterface.vst.IoMode
import io.github.durun.vst3kotlin.pluginterface.vst.ProcessMode
import io.github.durun.vst3kotlin.pluginterface.vst.ProcessSetup
import io.github.durun.vst3kotlin.pluginterface.vst.SymbolicSampleSize
import io.github.durun.vst3kotlin.vst.AudioProcessor
import io.github.durun.vst3kotlin.vst.Component
import io.github.durun.vst3kotlin.vst.ConnectionPoint

class AudioInstance(
	val component: Component,
	val processor: AudioProcessor,
	val connectionPoint: ConnectionPoint?
) : Closeable {
	companion object {
		private val log by logger()
		fun create(from: PluginFactory, classID: UID, ioMode: IoMode): AudioInstance {
			val component: Component = runCatching { from.createComponent(classID) }
				.onSuccess { log.info { "Created component" } }
				.onFailure { log.error { "Failed to create component" } }
				.getOrThrow()

			/** Created **/

			component.runCatching { setIoMode(ioMode) }
				.onSuccess { log.info { "Set IoMode $ioMode" } }
				.onFailure { log.warn { "Failed to set IoMode $ioMode" } }
				.getOrNull()

			runCatching { component.initialize() }
				.onSuccess { log.info { "Initialized component" } }
				.onFailure { log.error { "Failed to initialize component" } }
				.getOrThrow()

			/** Initialized **/

			val processor: AudioProcessor = runCatching { from.createAudioProcessor(classID) }
				.onSuccess { log.info { "Created AudioProcessor" } }
				.onFailure { log.error { "Failed to create AudioProcessor" } }
				.getOrThrow()

			runCatching {
				component.audioInputBusInfos.forEach { bus -> component.activate(bus, true) }
				component.audioOutputBusInfos.forEach { bus -> component.activate(bus, true) }
			}
				.onSuccess { log.info { "Activated audio bus" } }
				.onFailure { log.error { "Failed to activate audio bus" } }
				.getOrThrow()

			runCatching {
				component.eventInputBusInfos.forEach { bus -> component.activate(bus, true) }
				component.eventOutputBusInfos.forEach { bus -> component.activate(bus, true) }
			}
				.onSuccess { log.info { "Activated event bus" } }
				.onFailure { log.error { "Failed to activate event bus" } }
				.getOrThrow()

			// setup
			runCatching {
				processor.setupProcessing(
					ProcessSetup(
						processMode = ProcessMode.Realtime,
						sampleSize = SymbolicSampleSize.Sample32,
						maxSamplesPerBlock = 4096,
						sampleRate = 48000.0
					)
				)
			}
				.onSuccess { log.info { "SetupProcessing AudioProcessor" } }
				.onFailure { log.error { "Failed to SetupProcessing AudioProcessor" } }
				.getOrThrow()

			/** Setup Done **/

			runCatching { component.setActive(true) }
				.onSuccess { log.info { "SetActive Component" } }
				.onFailure { log.error { "Failed to SetActive Component" } }
				.getOrThrow()

			/** Activated **/

			runCatching { processor.setProcessing(true) }
				.onSuccess { log.info { "Set processing" } }
				.onFailure { log.error { "Failed to set processing" } }
				.getOrThrow()

			/** Processing **/

			return AudioInstance(component, processor, null)
		}
	}

	override var isOpen: Boolean = true
		private set

	override fun close() {
		check(isOpen)

		/** Processing **/

		runCatching { processor.setProcessing(false) }
			.onSuccess { log.info { "Unset processing" } }
			.onFailure { log.error { "Failed to unset processing" } }

		/** Activated **/

		runCatching { component.setActive(false) }
			.onSuccess { log.info { "Set Inactive Component" } }
			.onFailure { log.error { "Failed to Set inactive Component" } }

		/** Setup Done **/

		runCatching { component.terminate() }
			.onSuccess { log.info { "Terminated Component" } }
			.onFailure { log.error { "Failed to terminate Component" } }

		/** Created **/

		connectionPoint?.close()
		processor.close()
		component.close()

		isOpen = false
	}
}