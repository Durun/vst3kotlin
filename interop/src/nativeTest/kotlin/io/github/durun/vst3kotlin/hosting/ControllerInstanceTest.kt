package io.github.durun.vst3kotlin.hosting

import io.github.durun.resource.use
import io.github.durun.vst3kotlin.pluginterface.base.VstClassCategory
import io.github.durun.vst3kotlin.testResources
import kotlin.test.Test

class ControllerInstanceTest {

	val path = testResources.resolve("vst3/again.vst3")

	@Test
	fun test() {
		Module.of(path).use { module ->
			println("Module open.")
			val classInfo = module.factory.classInfo.first { it.category == VstClassCategory.AudioEffect }
			ControllerInstance.create(module.factory, classInfo.classId).use {
				println("ControllerInstance open.")
			}
			println("ControllerInstance closed.")
		}
		println("Module closed.")
	}
}