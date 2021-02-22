package io.github.durun.vst3kotlin.base

import cwrapper.FUID
import cwrapper.TUID
import kotlinx.cinterop.*

fun TUID.toUID(): UID {
	return UID(this.pointed.readValues(16).getBytes())
}

fun UID.toFuidPtr(scope: AutofreeScope): CPointer<FUID> {
	val fuid = cValue<FUID>().getPointer(scope)
	val tuid = fuid.pointed.tuid
	bytes.forEachIndexed { i, byte ->
		tuid[i] = byte
	}
	return fuid
}