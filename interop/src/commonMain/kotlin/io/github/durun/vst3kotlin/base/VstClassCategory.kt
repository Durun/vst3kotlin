package io.github.durun.vst3kotlin.base

enum class VstClassCategory(
	val value: String
) : CharSequence by value {
	AudioEffect("Audio Module Class"),
	ComponentController("Component Controller Class"),
	Test("Test Class")
}