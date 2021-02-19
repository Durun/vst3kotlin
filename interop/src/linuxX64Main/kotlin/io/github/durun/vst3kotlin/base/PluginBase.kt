package io.github.durun.vst3kotlin.base

import cwrapper.IPluginBase
import cwrapper.IPluginBase_initialize
import cwrapper.IPluginBase_terminate
import cwrapper.kResultTrue
import kotlinx.cinterop.CPointer
import kotlinx.cinterop.reinterpret

actual abstract class PluginBase(
	thisRawPtr: CPointer<*>
) : FUnknown(thisRawPtr) {
	private val thisPtr get() = thisRawPtr.reinterpret<IPluginBase>()
	actual fun initialize(context: HostContext?) {
		val result = IPluginBase_initialize(thisPtr, context?.ptr?.reinterpret())
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