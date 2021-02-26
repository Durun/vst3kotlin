package io.github.durun.vst3kotlin.hosting

import io.github.durun.io.Closeable
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
        @kotlin.ExperimentalUnsignedTypes
        fun create(from: PluginFactory, classID: UID, hostContext: HostCallback = HostCallback): PluginInstance {
            val component = from.createComponent(classID)
            runCatching { component.setIoMode(IoMode.Advanced) }
            component.initialize(hostContext)
            val processor = from.createAudioProcessor(classID)
            check(processor.canProcessSampleSize(SymbolicSampleSize.Sample32)) { "AudioProcessor can't process 32bit samples (cid=$classID)" }
            val controller = runCatching {
                component.queryEditController()
            }.recover {
                from.createEditController(component.controllerClassID)
            }.getOrThrow()
            controller.initialize(hostContext)
            // settings
            controller.setComponentHandler(ComponentHandler(hostContext.ptr))
            component.connectEach(controller)
            // set buses  // TODO: implement audio buffer
            component.audioInputBusInfos.forEach { bus ->
                component.activate(bus, true)
            }
            component.audioOutputBusInfos.forEach { bus ->
                component.activate(bus, true)
            }
            component.eventInputBusInfos.forEach { bus -> component.activate(bus, true) }
            component.eventOutputBusInfos.forEach { bus -> component.activate(bus, true) }
            // setup
            processor.setupProcessing(
                ProcessSetup(ProcessMode.Realtime, SymbolicSampleSize.Sample32, 512, 48000.0)
            )
            component.setActive(true)
            val plugView = controller.createView(ViewType.Editor)?.apply {
                check(isPlatformTypeSupported("HWND")) { "HWND isn't supported." }
                println("rect=$size, canResize=$canResize")
            }
            if (plugView==null) println("PlugView not implemented")
            processor.setProcessing(true)
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