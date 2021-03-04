package io.github.durun

import io.github.durun.vst3kotlin.hosting.Module
import io.github.durun.vst3kotlin.testResources
import kotlin.test.Test

class UseCase {

	fun getModuleClass(): Module.ModuleClass {
		return Module.open(testResources.resolve("vst3/TAL-Filter-2.vst3")).classes[0]
	}

	@Test
	fun case1() {
		val plugin = getModuleClass()
		plugin.createAudioInstance()
	}
}