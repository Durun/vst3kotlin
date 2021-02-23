package io.github.durun.vst3kotlin.vst

import cwrapper.*
import io.github.durun.vst3kotlin.cppinterface.CClass
import io.github.durun.vst3kotlin.base.FUnknown
import io.github.durun.vst3kotlin.base.kResultString
import kotlinx.cinterop.CPointer
import kotlinx.cinterop.CStructVar
import kotlinx.cinterop.reinterpret

class ConnectionPoint(
    override val ptr: CPointer<IConnectionPoint>
) : FUnknown() {
    fun connect(other: ConnectionPoint) {
        val result = IConnectionPoint_connect(this.ptr, other.ptr)
        check(result == kResultOk) { result.kResultString }
    }

    fun disconnect(other: ConnectionPoint) {
        val result = IConnectionPoint_disconnect(this.ptr, other.ptr)
        check(result == kResultOk) { result.kResultString }
    }

    fun notify(message: VstMessage) {
        val result = IConnectionPoint_notify(this.ptr, message.ptr)
        check(result == kResultOk) { result.kResultString }
    }
}

fun FUnknown.connectEach(other: FUnknown) {
    val thisPoint = this.queryInterface(IConnectionPoint_iid)
        .reinterpret<IConnectionPoint>()
        .let { ConnectionPoint(it) }
    val otherPoint = other.queryInterface(IConnectionPoint_iid)
        .reinterpret<IConnectionPoint>()
        .let { ConnectionPoint(it) }
    thisPoint.connect(otherPoint)
    otherPoint.connect(thisPoint)
}