package io.github.durun.vst3kotlin.base

import io.github.durun.io.use
import io.github.durun.vst3kotlin.Vst3Package
import io.github.durun.vst3kotlin.cppinterface.HostCallback
import io.github.durun.vst3kotlin.testResources
import kotlin.test.Test

class PluginBaseTest {
	val path = testResources.resolve("vst3/again.vst3")

	@Test
	fun initialize() {
		Vst3Package.open(path).use { plugin ->
			plugin.openPluginFactory().use { factory ->
				val cid = factory.classInfo.first().classId
				factory.createComponent(cid).use { component ->
					component.initialize(HostCallback)
					component.terminate()
				}
			}
		}
	}
}