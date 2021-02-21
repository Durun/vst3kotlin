package io.github.durun.vst3kotlin.base

import io.github.durun.vst3kotlin.vst.AudioProcessor
import io.github.durun.vst3kotlin.vst.Component
import io.github.durun.vst3kotlin.vst.EditController

expect class PluginFactory : FUnknown {
	val factoryInfo: FactoryInfo
	val classInfo: List<ClassInfo>
	override fun close()
	fun createComponent(classID: UID): Component
	fun createAudioProcessor(classID: UID): AudioProcessor
	fun createEditController(classID: UID): EditController
}
