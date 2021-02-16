package io.github.durun.vst3kotlin.base

import kotlinx.cinterop.getBytes
import kotlinx.cinterop.pointed
import kotlinx.cinterop.readValues

fun cwrapper.TUID.toTUID(): TUID {
	return TUID(this.pointed.readValues(16).getBytes())
}
