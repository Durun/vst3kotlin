package io.github.durun.vst3kotlin.base

import cwrapper.*
import cwrapper.FUnknown
import io.github.durun.io.Closeable
import io.github.durun.vst3kotlin.InterfaceID
import io.github.durun.vst3kotlin.VstInterface
import kotlinx.cinterop.*

actual abstract class FUnknown(
	protected val thisRawPtr: CPointer<*>
) : Closeable {
	private val ptr: CPointer<FUnknown> get() = thisRawPtr.reinterpret()
	actual final override var isOpen: Boolean = true
		private set

	actual override fun close() {
		check(isOpen)
		FUnknown_release(ptr)
		isOpen = false
	}

	fun queryInterface(interfaceID: TUID): CPointer<*> {
		return memScoped {
			val obj = alloc<CPointerVar<*>>()
			val result = IPluginFactory_queryInterface( // TODO: use FUnknown_queryInterface
				ptr.reinterpret(),
				interfaceID,
				obj.ptr
			)
			check(result == kResultTrue) {
				when (result) {
					kNoInterface -> {
						val uid = interfaceID.toUID()
						"NoInterface: ${InterfaceID.of(uid) ?: uid}"
					}
					else -> result.kResultString
				}
			}
			val ptr = obj.value
			checkNotNull(ptr)
			ptr
		}
	}

	fun <I : CPointed> queryVstInterface(interfaceID: TUID): VstInterface<CPointer<I>> {
		val ptr = queryInterface(interfaceID)
			.reinterpret<I>()
		return VstInterface(ptr)
	}
}