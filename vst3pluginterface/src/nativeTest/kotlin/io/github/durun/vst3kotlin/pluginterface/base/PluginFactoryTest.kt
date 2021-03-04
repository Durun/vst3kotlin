package io.github.durun.vst3kotlin.pluginterface.base

import io.github.durun.resource.use
import io.github.durun.vst3kotlin.Module
import io.github.durun.vst3kotlin.testResources
import io.kotest.matchers.shouldBe
import kotlin.test.Test

class PluginFactoryTest {
	val path = testResources.resolve("vst3/again.vst3")

	@Test
	fun factoryInfo() {
		val info = Module.open(path).use { plugin ->
			plugin.factory.factoryInfo
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
		val classes = Module.open(path).use { plugin ->
			plugin.factory.classInfo
		}
		println(classes.joinToString("\n"))
		classes shouldBe listOf(
			ClassInfo(
				classId = UID("84E8DE5F92554F5396FAE4133C935A18"),
				cardinality = 2147483647,
				category = VstClassCategory.AudioEffect,
				name = "AGain VST3"
			),
			ClassInfo(
				classId = UID("D39D5B65D7AF42FA843F4AC841EB04F0"),
				cardinality = 2147483647,
				category = VstClassCategory.ComponentController,
				name = "AGain VST3Controller"
			),
			ClassInfo(
				classId = UID("41347FD6FED64094AFBB12B7DBA1D441"),
				cardinality = 2147483647,
				category = VstClassCategory.AudioEffect,
				name = "AGain SideChain VST3"
			)
		)
	}
}