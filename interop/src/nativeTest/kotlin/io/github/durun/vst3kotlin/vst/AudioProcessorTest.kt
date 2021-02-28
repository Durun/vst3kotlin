package io.github.durun.vst3kotlin.vst

import cwrapper.ProcessContext
import cwrapper.ProcessData
import io.github.durun.io.use
import io.github.durun.vst3kotlin.base.VstClassCategory
import io.github.durun.vst3kotlin.hosting.Module
import io.github.durun.vst3kotlin.testResources
import kotlinx.cinterop.*
import kotlin.test.Test

class AudioProcessorTest {
    val path = testResources.resolve("vst3/again.vst3")

    @kotlin.ExperimentalStdlibApi
    @kotlin.ExperimentalUnsignedTypes
    @Test
    fun processWithParameters() {
        memScoped {
            val framePos = 0L
            val duration = 16
            val context = alloc<ProcessContext>().processContextOf(
                playing = true,
                sampleRate = 48000.0,
                projectTimeSamples = framePos,
                tempo = 120.0,
                timeSig = 4 over 4
            )

            val data = alloc<ProcessData>().processDataOf(
                context = context.ptr,
                mode = ProcessMode.Realtime,
                inputAudio = FloatAudioBusBuffer(length = duration, numChannels = 1)
                    .apply { channels[0]?.set(0, 0.1f) },
                outputAudio = FloatAudioBusBuffer(length = duration, numChannels = 1),
                inputParam = buildParameterChanges {
                    /** Sample offset を指定しない **/
                    put(paramID = 0u, value = 4.0)
                    /** Sample offset を指定する **/
                    put(paramID = 1u) {
                        put(sampleOffset = 1, value = 0.0)
                        put(sampleOffset = 2, value = 1.0)
                        put(sampleOffset = 3, value = 0.0)
                        put(sampleOffset = 4, value = 1.0)
                    }
                }.placeToCInterface(this@memScoped)
            )

            Module.of(path).use {
                val clazz = it.classes.find { it.info.category == VstClassCategory.AudioEffect }!!
                clazz.createInstance().use { instance ->
                    instance.controller.apply {
                        println(this.parameterInfo.joinToString("\n"))
                    }
                    instance.processor.apply {
                        println(inputBusArrangements)
                        println(outputBusArrangements)
                        println(processContextRequirement)
                    }
                    instance.processor.process(data)
                }
            }

            data.run {
                listOf(inputs, outputs)
            }.map {
                it?.pointed?.channelBuffers32?.get(0)
                    ?.let { samples -> (0 until duration).map { i -> samples[i] } }
            }.forEach {
                println(it)
            }
        }
    }
}