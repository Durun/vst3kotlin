package io.github.durun.vst3kotlin.base

import cwrapper.IPluginBase
import cwrapper.IPluginBase_initialize
import cwrapper.IPluginBase_terminate
import cwrapper.kResultTrue
import io.github.durun.vst3kotlin.cppinterface.HostCallback
import kotlinx.cinterop.CPointer
import kotlinx.cinterop.ptr
import kotlinx.cinterop.reinterpret

actual abstract class PluginBase(
	thisRawPtr: CPointer<*>
) : FUnknown(thisRawPtr) {
	private val thisPtr get() = thisRawPtr.reinterpret<IPluginBase>()
	actual fun initialize() {
		val context = HostCallback.cClass.ptr
		val result = IPluginBase_initialize(thisPtr, context.reinterpret())
		check(result == kResultTrue) {
			terminate()
			"${result.kResultString} on initialize"
		}
	}

	actual fun terminate() {
		val result = IPluginBase_terminate(thisPtr)
		check(result== kResultTrue) {"${result.kResultString} on terminate"}
	}
}