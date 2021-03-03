package io.github.durun.vst3kotlin.hosting

import io.github.durun.resource.use
import io.github.durun.vst3kotlin.pluginterface.base.VstClassCategory
import io.github.durun.vst3kotlin.testResources
import kotlin.test.Test

class ControllerInstanceTest {

	val path = testResources.resolve("vst3/TAL-NoiseMaker.vst3")

	@Test
	fun test() {
		Module.of(path).use { module ->
			println("Module open.")
			module.classes.first { it.info.category == VstClassCategory.AudioEffect }.createControllerInstance().use {
				println("ControllerInstance open.")
			}
			println("ControllerInstance closed.")
		}
		println("Module closed.")
	}
}