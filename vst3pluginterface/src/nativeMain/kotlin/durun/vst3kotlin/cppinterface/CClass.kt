package io.github.durun.vst3kotlin.cppinterface

import kotlinx.cinterop.CPointer
import kotlinx.cinterop.CStructVar

actual interface CClass {
	val ptr: CPointer<out CStructVar>
}