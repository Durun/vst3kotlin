package io.github.durun.vst3kotlin.base

import cwrapper.*
import io.github.durun.util.ByteArrayReader
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
				objPtr?.pointed?.value = TODO()
				when (uid) {
					IComponentHandler2_iid-> kResultOk
					else -> kNoInterface
				}
			}
		vtable.pointed.beginEdit =
			staticCFunction { _: COpaquePointer?, id: UInt ->
				println("callback beginEdit: id=$id")
				val message = Message.BeginEdit.bytesOf(id)
				memScoped {
					val values = message.toCValues()
					MessageQueue_enqueue(values, values.size)
				}
				kResultOk
			}
		vtable.pointed.performEdit =
			staticCFunction { _: COpaquePointer?, id: UInt, valueNormalized: Double ->
				kNotImplemented
			}
		vtable.pointed.endEdit =
			staticCFunction { _: COpaquePointer?, id: UInt ->
				println("callback endEdit: id=$id")
				val message = Message.EndEdit.bytesOf(id)
				memScoped {
					val values = message.toCValues()
					MessageQueue_enqueue(values, values.size)
				}
				kNotImplemented
			}
		vtable.pointed.restartComponent =
			staticCFunction { _: COpaquePointer?, flags: Int ->
				kNotImplemented
			}
	}

	actual fun receiveMessages(): List<Message> {
		return memScoped {
			val buf = allocArray<ByteVar>(MessageQueueLength)
			val readSize = MessageQueue_dequeue(buf, MessageQueueLength)
			val bytes = (0 until readSize).map {
				buf[it]
			}.toByteArray()
			Message.decodeAll(ByteArrayReader(bytes), readSize)
		}
	}
}
