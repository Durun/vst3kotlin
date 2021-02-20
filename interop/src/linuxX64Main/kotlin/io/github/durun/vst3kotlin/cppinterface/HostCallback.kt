package io.github.durun.vst3kotlin.cppinterface

import cwrapper.*
import io.github.durun.io.Closeable
import io.github.durun.util.allocByteQueue
import io.github.durun.util.allocLongArrayStore
import io.github.durun.util.enqueue
import io.github.durun.util.use
import kotlinx.cinterop.*


private fun allocComponentHandler(placement: NativeFreeablePlacement): SIComponentHandler {
	val struct: SIComponentHandler = placement.alloc()
	val vtable: IComponentHandlerVTable = placement.alloc()
	struct.vtable = vtable.ptr
	return struct
}

fun IComponentHandler.free(placement: NativeFreeablePlacement) {
	this.vtable?.let {
		placement.free(it.pointed.rawPtr)
	}
	placement.free(this.rawPtr)
}

@kotlin.ExperimentalUnsignedTypes
object HostCallback : Closeable {
	private val store: CPointer<LongArrayStore> = allocLongArrayStore(nativeHeap, 16)
	private val queue: CPointer<ByteQueue> = allocByteQueue(nativeHeap)
	val cClass: IComponentHandler

	// store index
	private const val refCount: Int = 1
	private const val thisPtrVar: Int = 2
	private const val beginEditId: Int = 3

	init {
		cClass = defineClass(refCount, thisPtrVar)
	}

	private fun defineClass(refCountIndex: Int, thisPtrIndex: Int): IComponentHandler {
		val struct = allocComponentHandler(nativeHeap)
		store.use {
			it[refCountIndex] = 0
			it[thisPtrIndex] = struct.ptr.toLong()
		}
		val vtable = struct.vtable?.pointed
		checkNotNull(vtable)
		vtable.FUnknown.addRef = define_addref()
		vtable.FUnknown.release = define_release()
		vtable.FUnknown.queryInterface = define_queryInterface()
		return struct.reinterpret()
	}

	private fun define_addref(): CPointer<CFunction<(COpaquePointer?) -> uint32>> = staticCFunction { _ ->
		// TODO: Is closed check
		store.use {
			it[refCount]++
			it[refCount].toUInt()
		}
	}

	private fun define_release(): CPointer<CFunction<(COpaquePointer?) -> uint32>> = staticCFunction { _ ->
		store.use {
			it[refCount]--
			it[refCount].toUInt()
		}
	}

	private fun define_queryInterface(): CPointer<CFunction<(COpaquePointer?, TUID?, CPointer<COpaquePointerVar>?) -> tresult>> =
		staticCFunction { _, uid, obj ->
			val isOk = when (uid) {
				FUnknown_iid -> true
				IComponentHandler_iid -> true
				IComponentHandler2_iid -> true
				else -> false
			}
			if (isOk) {
				obj?.pointed?.value = store.use { it[thisPtrVar] }.toCPointer()
				kResultOk
			} else kNoInterface
		}


	private fun beginEdit(id: UInt): CPointer<CFunction<(COpaquePointer?, UInt) -> tresult>> =
		staticCFunction { _, id ->
			queue.enqueue(Message.BeginEdit.bytes(id))
			kResultOk
		}

	private fun performEdit(
		id: UInt,
		valueNormalized: Double
	): CPointer<CFunction<(COpaquePointer?, UInt, Double) -> tresult>> =
		staticCFunction { _, id, value ->
			queue.enqueue(Message.PerformEdit.bytes(id, value))
			kResultOk
		}

	private fun endEdit(id: UInt): CPointer<CFunction<(COpaquePointer?, UInt) -> tresult>> =
		staticCFunction { _, id ->
			queue.enqueue(Message.EndEdit.bytes(id))
			kResultOk
		}

	private fun restartComponent(flags: Int): CPointer<CFunction<(COpaquePointer?, Int) -> tresult>> =
		staticCFunction { _, flags ->
			queue.enqueue(Message.RestartComponent.bytes(flags))
			kResultOk
		}

	private fun setDirty(state: Boolean): CPointer<CFunction<(COpaquePointer?, Boolean) -> tresult>> =
		staticCFunction { _, state ->
			queue.enqueue(Message.SetDirty.bytes(state))
			kResultOk
		}

	private fun requestOpenEditor(name: String): CPointer<CFunction<(COpaquePointer?, String) -> tresult>> =
		staticCFunction { _, name ->
			queue.enqueue(Message.RequestOpenEditor.bytes(name))
			kResultOk
		}

	private fun startGroupEdit(): CPointer<CFunction<(COpaquePointer?) -> tresult>> =
		staticCFunction { _ ->
			queue.enqueue(Message.StartGroupEdit.bytes())
			kResultOk
		}

	private fun finishGroupEdit(): CPointer<CFunction<(COpaquePointer?) -> tresult>> =
		staticCFunction { _ ->
			queue.enqueue(Message.FinishGroupEdit.bytes())
			kResultOk
		}

	override var isOpen: Boolean = true
		private set

	override fun close() {
		check(isOpen)
		cClass.free(nativeHeap)
		isOpen = false
	}
}