package io.github.durun.vst3kotlin

import io.github.durun.path.Path
import io.github.durun.resource.Closeable
import io.github.durun.vst3kotlin.base.PluginFactory

expect class Vst3Package : Closeable {
	companion object {
		fun open(vst3: Path): Vst3Package
	}

	override var isOpen: Boolean
		private set

	override fun close()
	fun openPluginFactory(): PluginFactory
}