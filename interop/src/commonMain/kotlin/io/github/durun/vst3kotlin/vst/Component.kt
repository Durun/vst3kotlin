package io.github.durun.vst3kotlin.vst

import io.github.durun.vst3kotlin.base.FUnknown

expect class Component : FUnknown {
	override var isOpen: Boolean
		private set

	override fun close()
	// TODO
}