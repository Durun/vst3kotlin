package io.github.durun.vst3kotlin.vst

import cwrapper.*
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

    @Test
    fun processWithoutParameters() {
        memScoped {
            val framePos = 0L
            val duration = 256
            val context = alloc<ProcessContext>()
                .apply {
                    tempo = 120.0
                    val beatPerSecond = tempo / 60
                    sampleRate = 48000.0
                    projectTimeSamples = framePos
                    projectTimeMusic = framePos / sampleRate * beatPerSecond
                    timeSigDenominator = 4
                    timeSigNumerator = 4
                    state = kPlaying or kProjectTimeMusicValid or kTempoValid or kTimeSigValid
                }
            val input = alloc<AudioBusBuffers>()
                .apply {
                    channelBuffers32 = allocArray<CPointerVar<Sample32Var>>(1)
                        .also {
                            it[0] = allocArray<FloatVarOf<Sample32>>(duration).also {
                                (0 until duration).forEach { i -> it[i] = 0.1f }
                            }
                        }
                    numChannels = 1
                    silenceFlags = 0u

                }
            val output = alloc<AudioBusBuffers>()
                .apply {
                    channelBuffers32 = allocArray<CPointerVar<Sample32Var>>(1)
                        .also { it[0] = allocArray(duration) }
                    numChannels = 1
                    silenceFlags = 0u

                }
            val data = alloc<ProcessData>()
                .apply {
                    processContext = context.ptr
                    processMode = ProcessMode.Realtime.value
                    symbolicSampleSize = SymbolicSampleSize.Sample32.value
                    numSamples = duration
                    numInputs = 1
                    numOutputs = 1
                    inputs = input.ptr
                    outputs = output.ptr
                }


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

            println((0 until duration).joinToString { input.channelBuffers32?.get(0)?.get(it).toString() })
            println((0 until duration).joinToString { output.channelBuffers32?.get(0)?.get(it).toString() })

        }
    }
}