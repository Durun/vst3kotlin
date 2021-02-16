package io.github.durun.vst3kotlin.vst

import io.github.durun.dylib.use
import io.github.durun.path.Path
import io.github.durun.vst3kotlin.Vst3Package
import io.github.durun.vst3kotlin.base.UID
import io.kotest.matchers.shouldBe
import kotlin.test.Test

class ComponentTest {
	val path = Path.of("src/commonTest/resources/vst3/again.vst3")

	@Test
	fun getControllerClassID() {
		Vst3Package.open(path).use { plugin ->
			plugin.openPluginFactory().use { factory ->
				val cid = factory.classInfo.first().classId
				factory.createComponent(cid).use { component ->
					component.controllerClassID shouldBe UID("D39D5B65D7AF42FA843F4AC841EB04F0")
				}
			}
		}
	}
}