package io.github.durun.vst3kotlin.base

import cwrapper.*
import io.github.durun.io.Closeable
import io.github.durun.vst3kotlin.VstInterface
import kotlinx.cinterop.*

actual abstract class FUnknown(
	protected val thisRawPtr: CPointer<*>
) : Closeable {
	private val thisPtr get() = thisRawPtr.reinterpret<cwrapper.FUnknown>()
	actual final override var isOpen: Boolean = true
		private set

	actual override fun close() {
		check(isOpen)
		FUnknown_release(thisPtr)
		isOpen = false
	}

	private fun queryInterface(interfaceID: TUID): CPointer<*> {
		return memScoped {
			val obj = alloc<CPointerVar<*>>()
			val result = IPluginFactory_queryInterface(
				thisPtr.reinterpret(),
				interfaceID,
				obj.ptr
			)    // TODO: use FUnknown_queryInterface
			check(result == kResultTrue) { result.kResultString }
			val ptr = obj.value
			checkNotNull(ptr)
			ptr
		}
	}

	fun <I : CPointed> queryInterface(): VstInterface<CPointer<I>> {
		val iid = IProcessContextRequirements_iid
		val ptr = queryInterface(iid).reinterpret<I>()
		return VstInterface(ptr)
	}
}