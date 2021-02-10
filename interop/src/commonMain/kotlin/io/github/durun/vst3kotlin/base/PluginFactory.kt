package io.github.durun.vst3kotlin.base

expect class PluginFactory : FUnknown {
	override var isOpen: Boolean
		private set
	val factoryInfo: FactoryInfo
	val classInfo: List<ClassInfo>
	override fun close()
}
