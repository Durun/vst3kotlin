package io.github.durun.vst3kotlin.base

import cwrapper.*
import kotlinx.cinterop.*


fun AutofreeScope.allocComponentHandler(): SIComponentHandler {
	val struct = alloc<SIComponentHandler>()
	val vtable = alloc<IComponentHandlerVTable>()
	struct.vtable = vtable.ptr
	return struct
}

@OptIn(ExperimentalUnsignedTypes::class)
actual class HostContext(thisPtr: CPointer<SIComponentHandler>) : FUnknown(thisPtr.reinterpret<IComponentHandler>()) {
	val ptr: CPointer<IComponentHandler> get() = thisRawPtr.reinterpret()
	var refCount: UInt = 0u

	init {
		val vtable = thisPtr.pointed.vtable
		checkNotNull(vtable)

		vtable.pointed.FUnknown.release =
			staticCFunction { _: COpaquePointer? ->
				//check(0u < refCount)
				//--refCount
				0u
			}
		vtable.pointed.FUnknown.addRef =
			staticCFunction { _: COpaquePointer? ->
				//++refCount
				1u
			}
		vtable.pointed.FUnknown.queryInterface =
			staticCFunction { _: COpaquePointer?, uid: TUID?, objPtr: CPointer<COpaquePointerVar>? ->
				println("callback queryInterface: uid=${uid?.toUID()}")
				when (uid) {
					IComponentHandler_iid-> {
						objPtr?.pointed?.value = ptr
						kResultOk
					}
					IComponentHandler2_iid-> kResultOk
					else -> kNoInterface
				}
			}
		vtable.pointed.beginEdit =
			staticCFunction { _: COpaquePointer?, id: UInt ->
				println("callback beginEdit: id=$id")
				kNotImplemented
			}
		vtable.pointed.performEdit =
			staticCFunction { _: COpaquePointer?, id: UInt, valueNormalized: Double ->
				kNotImplemented
			}
		vtable.pointed.endEdit =
			staticCFunction { _: COpaquePointer?, id: UInt ->
				println("callback endEdit: id=$id")
				kNotImplemented
			}
		vtable.pointed.restartComponent =
			staticCFunction { _: COpaquePointer?, flags: Int ->
				kNotImplemented
			}
	}
}
