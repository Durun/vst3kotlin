package io.github.durun.vst3kotlin.base

import io.github.durun.dylib.use
import io.github.durun.path.Path
import io.github.durun.vst3kotlin.Vst3Package
import kotlinx.cinterop.memScoped
import kotlinx.cinterop.ptr
import kotlinx.cinterop.reinterpret
import kotlin.test.Test
import io.github.durun.vst3kotlin.vst.ComponentHandler

class StructTest {
	@Test
	fun sPluginFactory() {
		val path = Path.of("src/commonTest/resources/vst3/again.vst3")
		Vst3Package.open(path).use { plugin ->
			plugin.openPluginFactory().use { factory ->
				val s = SPluginFactory(factory.ptr.reinterpret())
				val info = s.getFactoryInfo()
				println(info)
			}
		}
	}

	@Test
	fun callBack() {
		memScoped {
			val struct = allocComponentHandler()
			val context = HostContext(struct.ptr)
			println("created: $context")

			ComponentHandler(context.ptr).use {
				println("beginEdit...")
				it.beginEdit(114u)
				println("endEdit...")
				it.endEdit(514u)
			}
		}
	}
}