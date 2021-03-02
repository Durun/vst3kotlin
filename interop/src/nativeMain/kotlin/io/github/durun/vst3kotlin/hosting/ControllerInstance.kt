package io.github.durun.vst3kotlin.hosting

import io.github.durun.io.Closeable
import io.github.durun.log.logger
import io.github.durun.vst3kotlin.base.PluginFactory
import io.github.durun.vst3kotlin.base.UID
import io.github.durun.vst3kotlin.cppinterface.HostCallback
import io.github.durun.vst3kotlin.gui.PlugView
import io.github.durun.vst3kotlin.gui.ViewType
import io.github.durun.vst3kotlin.vst.Component
import io.github.durun.vst3kotlin.vst.ComponentHandler
import io.github.durun.vst3kotlin.vst.EditController
import io.github.durun.vst3kotlin.vst.connectEach
import io.github.durun.vst3kotlin.window.Window

class ControllerInstance(
	val component: Component,
	val controller: EditController,
	val plugView: PlugView?
) : Closeable {
	companion object {
		private val log by logger()

		fun create(from: PluginFactory, classID: UID, hostContext: HostCallback = HostCallback): ControllerInstance {

			val component: Component = runCatching { from.createComponent(classID) }
				.onSuccess { log.info { "Created component" } }
				.onFailure { log.error { "Failed to create component" } }
				.getOrThrow()

			val controller: EditController = runCatching { component.queryEditController() }
				.onSuccess { log.info { "Queried EditController" } }
				.onFailure { log.warn { "Failed to query EditController. Try create instead." } }
				.recoverCatching { from.createEditController(component.controllerClassID) }
				.onSuccess { log.info { "Created EditController" } }
				.onFailure { log.error { "Failed to create EditController" } }
				.getOrThrow()

			/** Created **/

			runCatching { component.initialize(hostContext) }
				.onSuccess { log.info { "Initialized component" } }
				.onFailure { log.error { "Failed to initialize component" } }
				.getOrThrow()
			runCatching { controller.setComponentHandler(ComponentHandler(hostContext.ptr)) }
				.onSuccess { log.info { "Set Callback as ComponentHandler to EditController" } }
				.onFailure { log.warn { "Failed to set Callback to EditController" } }
				.getOrThrow()

			controller.runCatching { initialize(hostContext) }
				.onSuccess { log.info { "Initialized EditController" } }
				.onFailure { log.warn { "Failed to create EditController" } }
				.getOrThrow()

			/** Initialized **/

			runCatching { component.connectEach(controller) }
				.onSuccess { log.info { "Connected Component and EditController" } }
				.onFailure { log.error { "Failed to connect Component and EditController" } }

			/** Connected **/

			val plugView = runCatching {
				val view: PlugView? = controller.createView(ViewType.Editor)
				checkNotNull(view)
			}
				.onSuccess { log.info { "Created PlugView" } }
				.onFailure { log.warn { "PlugView not available" } }
				.mapCatching { it.apply { check(isPlatformTypeSupported(Window.platformType)) } }
				.onSuccess { log.info { "${Window.platformType} is supported" } }
				.onFailure { log.warn { "${Window.platformType} isn't supported" } }
				.getOrNull()

			return ControllerInstance(component, controller, plugView)
		}
	}

	override var isOpen: Boolean = true
		private set

	override fun close() {
		check(isOpen)

		plugView?.close()
		controller.close()
		component.close()

		isOpen = false
	}
}