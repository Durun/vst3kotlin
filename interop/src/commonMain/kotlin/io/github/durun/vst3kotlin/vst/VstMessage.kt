package io.github.durun.vst3kotlin.vst

import io.github.durun.util.CClass
import io.github.durun.vst3kotlin.base.FUnknown

expect class VstMessage : FUnknown, CClass {
	var messageID: String
	val attributes: AttributeList
}