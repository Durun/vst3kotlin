package io.github.durun.vst3kotlin.vst

import cwrapper.*
import io.github.durun.io.use
import io.github.durun.vst3kotlin.base.VstClassCategory
import io.github.durun.vst3kotlin.hosting.Module
import io.github.durun.vst3kotlin.testResources
import kotlinx.cinterop.*
import kotlin.test.Test

class AudioProcessorTest {
    val path = testResources.resolve("vst3/again.vst3")

    @kotlin.ExperimentalStdlibApi
    @Test
    fun processWithParameters() {
        memScoped {
            val framePos = 0L
            val duration = 16
            val context = alloc<cwrapper.ProcessContext>()
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
            val data = alloc<cwrapper.ProcessData>()
                .apply {
                    processContext = context.ptr
                    processMode = ProcessMode.Realtime.value
                    symbolicSampleSize = SymbolicSampleSize.Sample32.value
                    numSamples = duration
                    numInputs = 1
                    numOutputs = 1

                    inputs = alloc<AudioBusBuffers>()
                        .apply {
                            channelBuffers32 = allocArray<CPointerVar<Sample32Var>>(1)
                                .also {
                                    it[0] = allocArray<Sample32Var>(duration).also {
                                        (0 until duration).forEach { i -> it[i] = 0.1f }
                                        /** 入力信号 (ずっと0.1) **/
                                    }
                                }
                            numChannels = 1
                            silenceFlags = 0u
                        }.ptr

                    inputParameterChanges = buildParameterChanges {
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

                    outputs = alloc<AudioBusBuffers>()
                        /** 出力バッファ **/
                        .apply {
                            channelBuffers32 = allocArray<CPointerVar<Sample32Var>>(1)
                                .also { it[0] = allocArray(duration) }
                            numChannels = 1
                            silenceFlags = 0u
                        }.ptr
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