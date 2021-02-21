package io.github.durun.vst3kotlin.cppinterface

import cwrapper.*
import io.github.durun.io.Closeable
import io.github.durun.util.*
import kotlinx.cinterop.*


private fun allocComponentHandler(): CPointer<SIComponentHandler> {
	val ptr = SIComponentHandler_alloc()
	checkNotNull(ptr) { "Failed to allocate SIComponentHandler" }
	return ptr.reinterpret()
}

private fun allocComponentHandler2(): CPointer<SIComponentHandler2> {
	val ptr = SIComponentHandler2_alloc()
	checkNotNull(ptr) { "Failed to allocate SIComponentHandler" }
	return ptr.reinterpret()
}

private fun CPointer<IComponentHandler>.free() {
	SIComponentHandler_free(this.reinterpret())
}

private fun CPointer<IComponentHandler2>.free() {
	SIComponentHandler2_free(this.reinterpret())
}


@kotlin.ExperimentalUnsignedTypes
actual object HostCallback : CClass, Closeable {
	private val store: CPointer<LongArrayStore> = allocLongArrayStore(nativeHeap, 16)
	private val queue: CPointer<ByteQueue> = allocByteQueue(nativeHeap)
	val ptr1: CPointer<IComponentHandler>
	val ptr2: CPointer<IComponentHandler2>
	override val ptr: CPointer<*> get() = ptr1

	// store index
	private const val refCount: Int = 1
	private const val thisPtr1: Int = 2
	private const val thisPtr2: Int = 3


	init {
		val ver1: CPointer<SIComponentHandler> = allocComponentHandler()
		val ver2: CPointer<SIComponentHandler2> = allocComponentHandler2()
		ptr1 = ver1.reinterpret()
		ptr2 = ver2.reinterpret()

		store.use {
			it[refCount] = 0
			it[thisPtr1] = ver1.toLong()
			it[thisPtr2] = ver2.toLong()
		}

		val vtable1 = ver1.pointed.vtable?.pointed
		val vtable2 = ver2.pointed.vtable?.pointed
		checkNotNull(vtable1)
		checkNotNull(vtable2)

		vtable1.defineIComponent()
		vtable2.defineIComponent2()
	}

	@kotlin.ExperimentalUnsignedTypes
	private fun IComponentHandlerVTable.defineIComponent() {
		FUnknown.defineFUnknown()
		beginEdit = define_beginEdit()
		performEdit = define_performEdit()
		endEdit = define_endEdit()
		restartComponent = define_restartComponent()
	}

	private fun IComponentHandler2VTable.defineIComponent2() {
		FUnknown.defineFUnknown()
		setDirty = define_setDirty()
		requestOpenEditor = define_requestOpenEditor()
		startGroupEdit = define_startGroupEdit()
		finishGroupEdit = define_finishGroupEdit()
	}

	@kotlin.ExperimentalUnsignedTypes
	private fun FUnknownVTable.defineFUnknown() {
		addRef = define_addref()
		release = define_release()
		queryInterface = define_queryInterface()
	}

	@kotlin.ExperimentalUnsignedTypes
	private fun define_addref(): CPointer<CFunction<(COpaquePointer?) -> uint32>> = staticCFunction { _ ->
		// TODO: Is closed check
		store.use {
			it[refCount]++
			it[refCount].toUInt()
		}
	}

	@kotlin.ExperimentalUnsignedTypes
	private fun define_release(): CPointer<CFunction<(COpaquePointer?) -> uint32>> = staticCFunction { _ ->
		store.use {
			it[refCount]--
			it[refCount].toUInt()
		}
	}

	private fun define_queryInterface(): CPointer<CFunction<(COpaquePointer?, TUID?, CPointer<COpaquePointerVar>?) -> tresult>> =
		staticCFunction { _, uid, obj ->
			when (uid) {
				FUnknown_iid -> kResultOk
				IComponentHandler_iid -> {
					obj?.pointed?.value = store.use { it[thisPtr1] }.toCPointer()
					kResultOk
				}
				IComponentHandler2_iid -> {
					obj?.pointed?.value = store.use { it[thisPtr2] }.toCPointer()
					kResultOk
				}
				else -> kNoInterface
			}
		}

	@kotlin.ExperimentalUnsignedTypes
	private fun define_beginEdit(): CPointer<CFunction<(COpaquePointer?, UInt) -> tresult>> =
		staticCFunction { _, id ->
			queue.enqueue(Message.BeginEdit.bytes(id))
			kResultOk
		}

	@kotlin.ExperimentalUnsignedTypes
	private fun define_performEdit(): CPointer<CFunction<(COpaquePointer?, UInt, Double) -> tresult>> =
		staticCFunction { _, id, value ->
			queue.enqueue(Message.PerformEdit.bytes(id, value))
			kResultOk
		}

	@kotlin.ExperimentalUnsignedTypes
	private fun define_endEdit(): CPointer<CFunction<(COpaquePointer?, UInt) -> tresult>> =
		staticCFunction { _, id ->
			queue.enqueue(Message.EndEdit.bytes(id))
			kResultOk
		}

	private fun define_restartComponent(): CPointer<CFunction<(COpaquePointer?, Int) -> tresult>> =
		staticCFunction { _, flags ->
			queue.enqueue(Message.RestartComponent.bytes(flags))
			kResultOk
		}

	private fun define_setDirty(): CPointer<CFunction<(COpaquePointer?, TBool) -> tresult>> =
		staticCFunction { _, state ->
			queue.enqueue(Message.SetDirty.bytes(state.toBoolean()))
			kResultOk
		}

	private fun define_requestOpenEditor(): CPointer<CFunction<(COpaquePointer?, FIDString?) -> tresult>> =
		staticCFunction { _, name ->
			if (name == null) return@staticCFunction kInvalidArgument
			queue.enqueue(Message.RequestOpenEditor.bytes(name.toKString()))
			kResultOk
		}

	private fun define_startGroupEdit(): CPointer<CFunction<(COpaquePointer?) -> tresult>> =
		staticCFunction { _ ->
			queue.enqueue(Message.StartGroupEdit.bytes())
			kResultOk
		}

	private fun define_finishGroupEdit(): CPointer<CFunction<(COpaquePointer?) -> tresult>> =
		staticCFunction { _ ->
			queue.enqueue(Message.FinishGroupEdit.bytes())
			kResultOk
		}

	override var isOpen: Boolean = true
		private set

	override fun close() {
		check(isOpen)
		ptr1.free()
		ptr2.free()
		store.free()
		queue.free()
		isOpen = false
	}

	actual fun receiveMessages(): Sequence<Message> = sequence {
		val bytes = queue.dequeue(ByteQueueLength)
		val messages: List<Message> = Message.decodeAll(bytes)
		yieldAll(messages)
	}
}