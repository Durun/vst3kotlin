package io.github.durun.vst3kotlin.base

expect class PluginFactory : FUnknown {
	val factoryInfo: FactoryInfo
	val classInfo: List<ClassInfo>
	override fun close()
}
