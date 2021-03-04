package io.github.durun.vst3kotlin.cppinterface

import cwrapper.*
import io.github.durun.resource.Closeable
import io.github.durun.resource.use
import kotlinx.cinterop.*


fun allocLongArrayStore(placement: NativePlacement, size: Int): CPointer<LongArrayStore> {
	val struct = placement.alloc<LongArrayStore>()
	struct.array = placement.allocArray(size)
	LongArrayStore_init(struct.ptr, size)
	return struct.ptr
}

fun CPointer<LongArrayStore>.free() {
	LongArrayStore_delete(this)
}

class LongArrayStoreScope(
	private val ptr: CPointer<LongArrayStore>
) : Closeable {
	operator fun get(index: Int): Long = LongArrayStore_read(ptr, index)
	operator fun set(index: Int, value: Long) = LongArrayStore_write(ptr, index, value)
	override var isOpen: Boolean = true
		private set

	init {
		LongArrayStore_enter(ptr)
	}

	override fun close() {
		LongArrayStore_exit(ptr)
	}
}

fun <R> CPointer<LongArrayStore>.use(block: (LongArrayStoreScope) -> R): R {
	return LongArrayStoreScope(this).use(block)
}