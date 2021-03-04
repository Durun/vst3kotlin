package io.github.durun.vst3kotlin.hosting

import io.github.durun.resource.use
import io.github.durun.vst3kotlin.pluginterface.base.VstClassCategory
import io.github.durun.vst3kotlin.testResources
import kotlin.test.Test

class ControllerInstanceTest {

	val path = testResources.resolve("vst3/TAL-NoiseMaker.vst3")

	@Test
	fun test() {
		val repo: VstClassRepository = VstFile.of(path)
		val classId = repo.classInfos.first { it.category == VstClassCategory.AudioEffect }.classId
		val vstClass = repo[classId] ?: error("No Audio effect")
		vstClass.createAudioInstance().use {
			println("ControllerInstance open.")
		}
		println("ControllerInstance closed.")
	}
}