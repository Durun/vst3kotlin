package io.github.durun.vst3kotlin.hosting

import io.github.durun.resource.Closeable
import io.github.durun.resource.Shared
import io.github.durun.util.logger
import io.github.durun.vst3kotlin.cppinterface.HostCallback
import io.github.durun.vst3kotlin.pluginterface.base.PluginFactory
import io.github.durun.vst3kotlin.pluginterface.base.UID
import io.github.durun.vst3kotlin.pluginterface.gui.PlugView
import io.github.durun.vst3kotlin.pluginterface.gui.ViewType
import io.github.durun.vst3kotlin.pluginterface.vst.Component
import io.github.durun.vst3kotlin.pluginterface.vst.ComponentHandler
import io.github.durun.vst3kotlin.pluginterface.vst.EditController
import io.github.durun.vst3kotlin.pluginterface.vst.connectEach
import io.github.durun.window.Window

/**
 * VSTプラグインの、コントローラ側のインスタンスです。
 * [VstClass]から作られます。
 * 使用後には[close]が呼ばれる必要があります。
 * @sample io.github.durun.vst3kotlin.samples.controllerInstanceSample
 */
class ControllerInstance(
	private val component: Component,
	private val controller: EditController,
	val plugView: PlugView?,
	private val toClose: Shared<*>
) : Closeable {
	companion object {
		private val log by logger()

		internal fun create(factory: PluginFactory, classID: UID, toClose: Shared<*>, hostContext: HostCallback = HostCallback): ControllerInstance {

			val component: Component = runCatching { factory.createComponent(classID) }
				.onSuccess { log.info { "Created component" } }
				.onFailure { log.error { "Failed to create component" } }
				.getOrThrow()

			val controller: EditController = runCatching { component.queryEditController() }
				.onSuccess { log.info { "Queried EditController" } }
				.onFailure { log.warn { "Failed to query EditController. Try create instead." } }
				.recoverCatching { factory.createEditController(component.controllerClassID) }
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

			return ControllerInstance(component, controller, plugView, toClose)
		}
	}

	override var isOpen: Boolean = true
		private set

	override fun close() {
		check(isOpen)

		plugView?.close()
		controller.close()
		component.close()
		toClose.close()

		isOpen = false
	}
}