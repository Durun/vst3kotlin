package io.github.durun.vst3kotlin.base

enum class VstClassCategory(
	private val value: String
) : CharSequence by value {
	AudioEffect("Audio Module Class"),
	ComponentController("Component Controller Class")
}