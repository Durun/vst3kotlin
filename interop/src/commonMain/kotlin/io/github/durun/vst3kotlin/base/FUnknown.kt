package io.github.durun.vst3kotlin.base

import io.github.durun.io.Closeable

expect abstract class FUnknown: Closeable {
	final override var isOpen: Boolean
		private set
	override fun close()
}