package io.github.durun.vst3kotlin.base

import cwrapper.IPluginBase
import cwrapper.IPluginBase_initialize
import cwrapper.IPluginBase_terminate
import cwrapper.kResultTrue
import io.github.durun.util.CClass
import kotlinx.cinterop.CPointer
import kotlinx.cinterop.reinterpret

abstract class PluginBase(thisRawPtr: CPointer<*>) : FUnknown(thisRawPtr) {
	private val ptr: CPointer<IPluginBase> get() = thisRawPtr.reinterpret()
	fun initialize(context: CClass) {
		val result = IPluginBase_initialize(ptr, context.ptr.reinterpret())
		check(result == kResultTrue) {
			terminate()
			"${result.kResultString} on initialize"
		}
	}

	fun terminate() {
		val result = IPluginBase_terminate(ptr)
		check(result == kResultTrue) { "${result.kResultString} on terminate" }
	}
}