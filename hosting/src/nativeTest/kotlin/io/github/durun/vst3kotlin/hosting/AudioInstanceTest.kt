package io.github.durun.vst3kotlin.hosting

import cwrapper.ProcessContext
import cwrapper.ProcessData
import io.github.durun.resource.use
import io.github.durun.vst3kotlin.pluginterface.base.VstClassCategory
import io.github.durun.vst3kotlin.pluginterface.vst.*
import io.github.durun.vst3kotlin.testResources
import io.kotest.matchers.shouldBe
import kotlinx.cinterop.*
import kotlin.test.Test

class AudioInstanceTest {
	val path = testResources.resolve("vst3/again.vst3")

	@Test
	fun openTwice() {
		(1..2).forEach { _ ->
			Module.open(path).use { module ->
				println("Module open.")
				val classInfo = module.factory.classInfo.first { it.category == VstClassCategory.AudioEffect }
				AudioInstance.create(module.factory, classInfo.classId, IoMode.Advanced).use {
					println("AudioInstance open.")
				}
				println("AudioInstance closed.")
			}
			println("Module closed.")
		}
	}

	@Test
	fun test() {
		val framePos = 0L
		val duration = 4
		val outBuffer = FloatAudioBusBuffer(length = duration, numChannels = 1)
		val inBuffer = FloatAudioBusBuffer(length = duration, numChannels = 1)
			.apply {
				/** input signal **/
				channels[0]?.let {
					it[1] = 0.1f
					it[2] = 0.2f
				}
			}
		memScoped {
			val context = alloc<ProcessContext>().processContextOf(
				playing = true,
				sampleRate = 48000.0,
				projectTimeSamples = framePos,
				tempo = 120.0,      // optional
				timeSig = 4 over 4  // optional
			)
			val data = alloc<ProcessData>().processDataOf(
				context = context.ptr,
				mode = ProcessMode.Realtime,
				inputAudio = inBuffer,
				outputAudio = outBuffer,
				inputParam = buildParameterChanges {
					/** parameters (Gain) **/
					/** parameters (Gain) **/
					put(paramID = 0u, value = 2.0)
				}.placeToCInterface(this)
			)

			/** Open VST here **/
			Module.open(path).use { module ->
				println("Module open.")
				val classInfo = module.factory.classInfo.first { it.category == VstClassCategory.AudioEffect }
				AudioInstance.create(module.factory, classInfo.classId, IoMode.Advanced).use {
					it.process(data)
					println("Instance open.")
				}
				println("Instance closed.")
			}
			println("Module closed.")


			val (inData, outData) = data.run {
				listOf(inputs, outputs)
			}.map {
				it?.pointed?.channelBuffers32?.get(0)
					?.let { samples -> (0 until duration).map { i -> samples[i] } }
			}
			println("input : $inData")
			println("output: $outData")
			inData shouldBe listOf(0f, 0.1f, 0.2f, 0f)
			outData shouldBe listOf(0f, 0.2f, 0.4f, 0f)
		}
		inBuffer.close()
		outBuffer.close()
	}
}