package io.github.durun.vst3kotlin.base

import cwrapper.IPluginBase
import cwrapper.IPluginBase_initialize
import cwrapper.IPluginBase_terminate
import cwrapper.kResultTrue
import io.github.durun.vst3kotlin.cppinterface.CClass
import kotlinx.cinterop.CPointer
import kotlinx.cinterop.reinterpret

abstract class PluginBase : FUnknown() {
	private val thisPtr: CPointer<IPluginBase> get() = ptr.reinterpret()

	private var initialized: Boolean = false

	override fun close() {
		if (initialized) terminate()
		initialized = false
		super.close()
	}

	fun initialize(context: CClass? = null) {
		val result = IPluginBase_initialize(thisPtr, context?.ptr?.reinterpret())
		check(result == kResultTrue) {
			terminate()
			"${result.kResultString} on initialize"
		}
		initialized = true
	}

	fun terminate() {
		val result = IPluginBase_terminate(thisPtr)
		check(result == kResultTrue) { "${result.kResultString} on terminate" }
	}
}