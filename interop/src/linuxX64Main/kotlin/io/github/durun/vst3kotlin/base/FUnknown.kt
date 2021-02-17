package io.github.durun.vst3kotlin.base

import cwrapper.FUnknown_queryInterface
import cwrapper.FUnknown_release
import cwrapper.IProcessContextRequirements
import cwrapper.kResultTrue
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

	private fun queryInterface(interfaceID: UID): CPointer<*> {
		return memScoped {
			val iid = interfaceID.toFuidPtr(this).pointed.tuid
			val obj = alloc<CPointerVar<*>>()
			val result = FUnknown_queryInterface(thisPtr, iid, obj.ptr)
			check(result == kResultTrue) { result.kResultString }
			val ptr = obj.value
			checkNotNull(ptr)
			ptr
		}
	}

	fun <I : CPointed> queryInterface(): VstInterface<CPointer<I>> {
		val iid = VstInterface.IID.get<IProcessContextRequirements>()
		val ptr = queryInterface(iid).reinterpret<I>()
		return VstInterface(ptr)
	}
}