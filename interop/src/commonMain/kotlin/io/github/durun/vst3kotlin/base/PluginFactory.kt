package io.github.durun.vst3kotlin.base

import io.github.durun.vst3kotlin.vst.Component

expect class PluginFactory : FUnknown {
	val factoryInfo: FactoryInfo
	val classInfo: List<ClassInfo>
	override fun close()
	fun createComponent(classID: UID): Component
}
