package io.github.durun.vst3kotlin.base

import cwrapper.TUID
import kotlinx.cinterop.*

fun TUID.toUID(): UID {
    val bytes = this.pointed.readValues(16).getBytes()
    val component1 = bytes.sliceArray(0..3)
    val component2 = bytes.sliceArray(4..5)
    val component3 = bytes.sliceArray(6..7)
    val remain = bytes.sliceArray(8..15)
    component1.reverse()
    component2.reverse()
    component3.reverse()
    return UID(component1 + component2 + component3 + remain)
}

