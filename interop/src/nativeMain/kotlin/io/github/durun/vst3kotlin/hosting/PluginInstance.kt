package io.github.durun.vst3kotlin.hosting

import io.github.durun.io.Closeable
import io.github.durun.log.logger
import io.github.durun.vst3kotlin.base.PluginFactory
import io.github.durun.vst3kotlin.base.UID
import io.github.durun.vst3kotlin.cppinterface.HostCallback
import io.github.durun.vst3kotlin.gui.PlugView
import io.github.durun.vst3kotlin.gui.ViewType
import io.github.durun.vst3kotlin.vst.*

class PluginInstance
private constructor(
    val component: Component,
    val processor: AudioProcessor,
    val controller: EditController,
    val plugView: PlugView?
) : Closeable {
    companion object {
        private val log by logger()

        @kotlin.ExperimentalUnsignedTypes
        fun create(from: PluginFactory, classID: UID, hostContext: HostCallback = HostCallback): PluginInstance {

            val component = runCatching { from.createComponent(classID) }
                .onSuccess { log.info { "Created component" } }
                .onFailure { log.error { "Failed to create component" } }
                .getOrThrow()

            val ioMode = IoMode.Advanced
            runCatching { component.setIoMode(ioMode) }
                .onSuccess { log.info { "Set IoMode $ioMode" } }
                .onFailure { log.warn { "Failed to set IoMode $ioMode" } }

            runCatching { component.initialize(hostContext) }
                .onSuccess { log.info { "Initialized component" } }
                .onFailure { log.error { "Failed to initialize component" } }
                .getOrThrow()

            val processor = runCatching { from.createAudioProcessor(classID) }
                .onSuccess { log.info { "Created AudioProcessor" } }
                .onFailure { log.error { "Failed to create AudioProcessor" } }
                .getOrThrow()

            runCatching {
                check(processor.canProcessSampleSize(SymbolicSampleSize.Sample32))
            }
                .onSuccess { log.info { "AudioProcessor can process 32bit samples" } }
                .onFailure { log.error { "AudioProcessor can't process 32bit samples" } }
                .getOrThrow()

            val controller = runCatching {
                component.queryEditController()
            }.recover {
                from.createEditController(component.controllerClassID)
            }
                .onSuccess { log.info { "Created EditController" } }
                .onFailure { log.warn { "Failed to create EditController" } }
                .mapCatching { it.apply { initialize(hostContext) } }
                .onSuccess { log.info { "Initialized EditController" } }
                .onFailure { log.warn { "Failed to create EditController" } }
                .getOrThrow()

            // settings
            runCatching { controller.setComponentHandler(ComponentHandler(hostContext.ptr)) }
                .onSuccess { log.info { "Set Callback as ComponentHandler to EditController" } }
                .onFailure { log.warn { "Failed to set Callback to EditController" } }
                .getOrThrow()

            runCatching { component.connectEach(controller) }
                .onSuccess { log.info { "Connected Component and EditController" } }
                .onFailure { log.error { "Failed to connect Component and EditController" } }
                .getOrThrow()

            // set buses  // TODO: implement audio buffer
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
                        maxSamplesPerBlock = 512,
                        sampleRate = 48000.0
                    )
                )
            }
                .onSuccess { log.info { "SetupProcessing AudioProcessor" } }
                .onFailure { log.error { "Failed to SetupProcessing AudioProcessor" } }
                .getOrThrow()

            runCatching { component.setActive(true) }
                .onSuccess { log.info { "SetActive Component" } }
                .onFailure { log.error { "Failed to SetActive Component" } }
                .getOrThrow()

            val platformType = "HWND" // TODO: type for multiplatform
            val plugView = runCatching {
                val view: PlugView? = controller.createView(ViewType.Editor)
                checkNotNull(view)
            }
                .onSuccess { log.info { "Created PlugView" } }
                .onFailure { log.warn { "PlugView not implemented" } }
                .mapCatching { it.apply { check(isPlatformTypeSupported(platformType)) } }
                .onSuccess { log.info { "$platformType is supported" } }
                .onFailure { log.warn { "$platformType isn't supported" } }
                .getOrNull()

            runCatching { processor.setProcessing(true) }
                .onSuccess { log.info { "Set processing" } }
                .onFailure { log.error { "Failed to set processing" } }
                .getOrThrow()

            return PluginInstance(component, processor, controller, plugView)
        }
    }

    override var isOpen: Boolean = true
        private set

    override fun close() {
        check(isOpen)
        processor.setProcessing(false)
        component.setActive(false)
        component.close()
        processor.close()
        isOpen = false
    }
}