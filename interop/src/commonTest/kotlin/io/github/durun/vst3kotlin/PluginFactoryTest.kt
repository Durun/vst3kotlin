package io.github.durun.vst3kotlin

import io.github.durun.dylib.use
import io.github.durun.path.Path
import io.github.durun.util.decodeAsBigEndian
import io.kotest.matchers.shouldBe
import kotlin.test.Test

class PluginFactoryTest {
	@Test
	fun factoryInfo() {
		val path = Path.of("src/commonTest/resources/vst3/again.vst3")
		val info = Vst3Package.open(path).use {plugin->
			plugin.openPluginFactory().use {
				it.factoryInfo
			}
		}
		println(info)
		info shouldBe FactoryInfo(
			vendor = "Steinberg Media Technologies",
			url = "http://www.steinberg.net",
			email = "mailto:info@steinberg.de",
			flags = FactoryInfo.Flags(
				NoFlags = false,
				ClassesDiscardable = false,
				LicenseCheck = false,
				ComponentNonDiscardable = false,
				Unicode = true
			)
		)
	}

	@Test
	fun classInfo() {
		val path = Path.of("src/commonTest/resources/vst3/again.vst3")
		val classes = Vst3Package.open(path).use { plugin ->
			plugin.openPluginFactory().use {
				it.classInfo
			}
		}
		println(classes.joinToString("\n"))
		classes shouldBe listOf(
			ClassInfo(
				classId = TUID("84E8DE5F92554F5396FAE4133C935A18".decodeAsBigEndian()),
				cardinality = 2147483647,
				category = "Audio Module Class",
				name = "AGain VST3"
			),
			ClassInfo(
				classId = TUID("D39D5B65D7AF42FA843F4AC841EB04F0".decodeAsBigEndian()),
				cardinality = 2147483647,
				category = "Component Controller Class",
				name = "AGain VST3Controller"
			),
			ClassInfo(
				classId = TUID("41347FD6FED64094AFBB12B7DBA1D441".decodeAsBigEndian()),
				cardinality = 2147483647,
				category = "Audio Module Class",
				name = "AGain SideChain VST3"
			)
		)
	}
}