package io.github.durun.vst3kotlin.vst

import io.github.durun.resource.use
import io.github.durun.vst3kotlin.Vst3Package
import io.github.durun.vst3kotlin.pluginterface.base.UID
import io.github.durun.vst3kotlin.testResources
import io.kotest.matchers.shouldBe
import kotlin.test.Test

class ComponentTest {
	val path = testResources.resolve("vst3/again.vst3")

	@Test
	fun getInfo() {
		Vst3Package.open(path).use { plugin ->
			plugin.openPluginFactory().use { factory ->
				val cid = factory.classInfo.first().classId
				factory.createComponent(cid).use { component ->
					component.apply {
						println("controllerClassID=$controllerClassID")
						println("audioInputBusInfos=$audioInputBusInfos")
						println("eventInputBusInfos=$eventInputBusInfos")
						println("audioOutputBusInfos=$audioOutputBusInfos")
						println("eventOutputBusInfos=$eventOutputBusInfos")
						println("routingInfo=$routingInfo")
						controllerClassID shouldBe UID("D39D5B65D7AF42FA843F4AC841EB04F0")
					}
				}
			}
		}
	}
}