package io.github.durun.vst3kotlin

import io.github.durun.io.Closeable
import io.github.durun.path.Path

expect class Vst3Package : Closeable {
	companion object {
		fun open(vst3: Path): Vst3Package
	}

	override fun close()

	val pluginFactory: PluginFactory
}

expect class PluginFactory {
	val factoryInfo: FactoryInfo
}

data class FactoryInfo(
	val vendor: String,
	val url: String,
	val email: String,
	val flags: Flags
) {
	data class Flags(
		val NoFlags: Boolean,
		val ClassesDiscardable: Boolean,
		val LicenseCheck: Boolean,
		val ComponentNonDiscardable: Boolean,
		val Unicode: Boolean
	)
}