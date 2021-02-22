package io.github.durun.vst3kotlin.vst

import io.github.durun.dylib.use
import io.github.durun.vst3kotlin.Vst3Package
import io.github.durun.vst3kotlin.base.VstClassCategory
import io.github.durun.vst3kotlin.testResources
import io.kotest.matchers.shouldBe
import kotlin.test.Test

class AudioProcessorTest {
	val path = testResources.resolve("vst3/hostchecker.vst3")

	@Test
	fun getInfo() {
		Vst3Package.open(path).use { plugin ->
			plugin.openPluginFactory().use { factory ->
				val cid = factory.classInfo.first { it.category == VstClassCategory.AudioEffect }.classId
				factory.createAudioProcessor(cid).use { proc ->
					println(proc.latencySampleSize)
					println(proc.tailSampleSize)
					println(proc.processContextRequirement)
					proc.latencySampleSize shouldBe 256
					proc.tailSampleSize shouldBe 256
					proc.processContextRequirement shouldBe ProcessContextRequirement(0b11111111111u)
				}
			}
		}
	}

	/*
	@Test
	fun getBusArrangement() {
		Vst3Package.open(testResources.resolve("vst3/hostchecker.vst3")).use { plugin ->
			plugin.openPluginFactory().use { factory ->
				val cid = factory.classInfo.first { it.category == VstClassCategory.AudioEffect }.classId
				factory.createAudioProcessor(cid).use { proc ->
					println(proc.inputBusArrangements)
					println(proc.outputBusArrangements)
				}
			}
		}
	}
	*/
}