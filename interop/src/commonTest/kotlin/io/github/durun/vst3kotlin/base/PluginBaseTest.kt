package io.github.durun.vst3kotlin.base

import io.github.durun.dylib.use
import io.github.durun.path.Path
import io.github.durun.vst3kotlin.Vst3Package
import kotlin.test.Test

class PluginBaseTest {
	val path = Path.of("src/commonTest/resources/vst3/again.vst3")

	@Test
	fun initialize() {
		Vst3Package.open(path).use { plugin ->
			plugin.openPluginFactory().use { factory ->
				val cid = factory.classInfo.first().classId
				factory.createComponent(cid).use { component ->
					component.initialize()
					component.terminate()
				}
			}
		}
	}
}