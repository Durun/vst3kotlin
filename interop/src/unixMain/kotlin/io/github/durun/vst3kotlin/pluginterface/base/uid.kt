package io.github.durun.vst3kotlin.pluginterface.base

import cwrapper.FUID
import cwrapper.TUID
import kotlinx.cinterop.*

fun TUID.toUID(): UID {
    return UID(this.pointed.readValues(16).getBytes())
}
fun UID.toFuidPtr(scope: AutofreeScope): CPointer<FUID> {
    val fuid = cValue<FUID>().getPointer(scope)
    val tuid = fuid.pointed.tuid
    tuid[0]= bytes[0]
    tuid[1]= bytes[1]
    tuid[2]= bytes[2]
    tuid[3]= bytes[3]
    tuid[4]= bytes[4]
    tuid[5]= bytes[5]
    tuid[6]= bytes[6]
    tuid[7]= bytes[7]
    tuid[8] = bytes[8]
    tuid[9] = bytes[9]
    tuid[10] = bytes[10]
    tuid[11] = bytes[11]
    tuid[12] = bytes[12]
    tuid[13] = bytes[13]
    tuid[14] = bytes[14]
    tuid[15] = bytes[15]
    return fuid
}