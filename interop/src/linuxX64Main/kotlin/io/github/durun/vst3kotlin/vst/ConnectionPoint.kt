package io.github.durun.vst3kotlin.vst

import cwrapper.*
import io.github.durun.vst3kotlin.base.FUnknown
import io.github.durun.vst3kotlin.base.kResultString
import kotlinx.cinterop.CPointer
import kotlinx.cinterop.reinterpret

actual class ConnectionPoint(thisPtr: CPointer<IConnectionPoint>) : FUnknown(thisPtr) {
	val ptr: CPointer<IConnectionPoint> get() = thisRawPtr.reinterpret()

	actual fun connect(other: ConnectionPoint) {
		val result = IConnectionPoint_connect(this.ptr, other.ptr)
		check(result == kResultOk) { result.kResultString }
	}

	actual fun disconnect(other: ConnectionPoint) {
		val result = IConnectionPoint_disconnect(this.ptr, other.ptr)
		check(result == kResultOk) { result.kResultString }
	}

	actual fun notify(message: VstMessage) {
		val result = IConnectionPoint_notify(this.ptr, message.ptr)
		check(result == kResultOk) { result.kResultString }
	}
}