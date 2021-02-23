package io.github.durun.vst3kotlin.base

import cwrapper.FUID
import cwrapper.TUID
import kotlinx.cinterop.*

fun TUID.toUID(): UID {
    return UID(this.pointed.readValues(16).getBytes())
}
