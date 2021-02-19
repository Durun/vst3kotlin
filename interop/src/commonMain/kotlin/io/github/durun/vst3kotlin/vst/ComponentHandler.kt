package io.github.durun.vst3kotlin.vst

import io.github.durun.vst3kotlin.base.FUnknown

expect class ComponentHandler:FUnknown {
	fun beginEdit(id: UInt)
	fun performEdit(id: UInt, valueNormalized: Double)
	fun endEdit(id: UInt)
	fun restartComponent(flags: Int)

	fun setDirty(state: Boolean)
	fun requestOpenEditor(name: String)
	fun startGroupEdit()
	fun finishGroupEdit()
}

