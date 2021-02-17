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

	fun queryInterface(interfaceID: TUID): CPointer<*> {
		return memScoped {
			val obj = alloc<CPointerVar<*>>()
			val result = IPluginFactory_queryInterface( // TODO: use FUnknown_queryInterface
				thisPtr.reinterpret(),
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