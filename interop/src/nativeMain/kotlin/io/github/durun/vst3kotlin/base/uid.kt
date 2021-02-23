package io.github.durun.vst3kotlin.base

import cwrapper.FUID
import kotlinx.cinterop.*

fun UID.toFuidPtr(scope: AutofreeScope): CPointer<FUID> {
    val fuid = cValue<FUID>().getPointer(scope)
    val tuid = fuid.pointed.tuid
    bytes.forEachIndexed { i, byte ->
        tuid[i] = byte
    }
    return fuid
}