package io.github.durun.vst3kotlin

import io.github.durun.resource.use
import io.github.durun.vst3kotlin.base.PluginFactory
import io.github.durun.vst3kotlin.cppinterface.HostCallback
import io.github.durun.vst3kotlin.pluginterface.base.UID
import io.github.durun.vst3kotlin.vst.Component
import io.github.durun.vst3kotlin.vst.EditController
import io.github.durun.vst3kotlin.vst.connectEach
import io.kotest.matchers.collections.shouldNotBeEmpty
import kotlin.test.Test

@ExperimentalUnsignedTypes
class SetupTest {
	val path = testResources.resolve("vst3/again.vst3")

	@Test
	fun setupSequence() {
		Vst3Package.open(path).use { plugin ->
			println("opening factory...")
			plugin.openPluginFactory().use { factory ->
				val cid = factory.classInfo.first().classId
				println("start setup: $cid")
				val (component, controller) = factory.setup(cid)

				controller.parameterInfo
					.onEach { println(it) }
					.shouldNotBeEmpty()
				component.eventInputBusInfos
					.onEach { println(it) }
					.shouldNotBeEmpty()
				component.audioInputBusInfos
					.onEach { println(it) }
					.shouldNotBeEmpty()
				component.audioOutputBusInfos
					.onEach { println(it) }
					.shouldNotBeEmpty()

				var gain = controller.getParamNormalized(0u)
				/*
				gain += 0.2
				println("set Gain $gain")
				controller.setParamNormalized(0u, gain)
				controller.getParamNormalized(0u) shouldBe gain
				println("receive messages...")
				HostCallback.receiveMessages()
					.forEach { println(it) }
				*/

				println("close controller...")
				controller.close()
				println("close component...")
				component.close()
			}
		}
	}

	private fun PluginFactory.setup(classID: UID): Pair<Component, EditController> {
		println("creating component...")
		val component = this.createComponent(classID)
		val controller = runCatching {
			println("initialize component...")
			component.initialize(HostCallback)
			println("query controller...")
			component.queryEditController()
		}.recoverCatching {
			val cid = component.controllerClassID
			println("create controller: $cid...")
			this.createEditController(cid)
				.also { it.initialize(HostCallback) }
		}.getOrThrow()
		println("connect component and controller...")
		component.connectEach(controller)
		return component to controller
	}
}