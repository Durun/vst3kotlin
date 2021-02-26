package io.github.durun.vst3kotlin.hosting

import io.github.durun.io.use
import io.github.durun.vst3kotlin.base.VstClassCategory
import io.github.durun.vst3kotlin.testResources
import kotlin.test.Test

class PluginInstanceTest {
	@Test
	fun setup() {
		val path = testResources.resolve("vst3/again.vst3")
		Module.of(path).use { module ->
			println("Module open.")
			val classInfo = module.factory.classInfo.first { it.category == VstClassCategory.AudioEffect }
			PluginInstance.create(module.factory, classInfo.classId).use {
				println("Instance open.")
			}
			println("Instance closed.")
		}
		println("Module closed.")
	}
}