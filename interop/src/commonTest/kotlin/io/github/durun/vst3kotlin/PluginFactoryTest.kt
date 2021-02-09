package io.github.durun.vst3kotlin

import io.github.durun.dylib.use
import io.github.durun.path.Path
import io.kotest.matchers.shouldBe
import kotlin.test.Test

class PluginFactoryTest {
	@Test
	fun factoryInfo() {
		val path = Path.of("src/commonTest/resources/vst3/again.vst3")
		val info = Vst3Package.open(path).use {
			it.pluginFactory.factoryInfo
		}
		println(info)
		info shouldBe FactoryInfo(
			vendor = "Steinberg Media Technologies",
			url = "http://www.steinberg.net",
			email="mailto:info@steinberg.de",
			flags = FactoryInfo.Flags(NoFlags=false, ClassesDiscardable=false, LicenseCheck=false, ComponentNonDiscardable=false, Unicode=true)
		)
	}
}