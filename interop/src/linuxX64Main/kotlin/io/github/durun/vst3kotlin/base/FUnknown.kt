package io.github.durun.vst3kotlin.base

import cwrapper.*
import cwrapper.FUnknown
import io.github.durun.io.Closeable
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
			check(result == kResultTrue) { result.kResultString }
			val ptr = obj.value
			checkNotNull(ptr)
			ptr
		}
	}

	inline fun <reified I : CPointed> queryInterface(): VstInterface<CPointer<I>> {
		val iid = when (I::class) {
			IComponent::class -> IComponent_iid
			IComponentHandler::class -> IComponentHandler_iid
			IComponentHandler2::class -> IComponentHandler2_iid
			IEditController::class -> IEditController_iid
			IEditController2::class -> IEditController2_iid
			IProcessContextRequirements::class -> IProcessContextRequirements_iid
			else -> throw IllegalArgumentException()
		}
		val ptr = queryInterface(iid).reinterpret<I>()
		return VstInterface(ptr)
	}
}