package io.github.durun.vst3kotlin.hosting

import cwrapper.ProcessData
import io.github.durun.resource.Closeable
import io.github.durun.resource.Shared
import io.github.durun.util.logger
import io.github.durun.vst3kotlin.pluginterface.base.PluginFactory
import io.github.durun.vst3kotlin.pluginterface.base.UID
import io.github.durun.vst3kotlin.pluginterface.vst.*

/**
 * VSTプラグインの、オーディオプロセッサ側のインスタンスです。
 * [VstClass]から作られます。
 * 使用後には[close]が呼ばれる必要があります。
 * @sample io.github.durun.vst3kotlin.samples.audioInstanceSample
 * @sample io.github.durun.vst3kotlin.samples.processDataSample
 */
class AudioInstance(
    private val component: Component,
    private val processor: AudioProcessor,
    private val connectionPoint: ConnectionPoint?,
    private val sharedPtr: Shared<*>? = null
) : Closeable {
    companion object {
        private val log by logger()
        internal fun create(
            factory: PluginFactory,
            classID: UID,
            ioMode: IoMode,
            toClose: Shared<Module>? = null
        ): AudioInstance {
            val component: Component = runCatching { factory.createComponent(classID) }
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

            val processor: AudioProcessor = runCatching { factory.createAudioProcessor(classID) }
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

            return AudioInstance(component, processor, null, toClose)
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
        sharedPtr?.close()

        isOpen = false
    }

    fun process(data: ProcessData) {
        processor.process(data)
    }
}