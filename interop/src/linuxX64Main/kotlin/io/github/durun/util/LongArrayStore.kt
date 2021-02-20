package io.github.durun.util

import cwrapper.*
import io.github.durun.dylib.use
import io.github.durun.io.Closeable
import kotlinx.cinterop.*


fun allocLongArrayStore(placement: NativeFreeablePlacement, size:Int): CPointer<LongArrayStore> {
	val struct = placement.alloc<LongArrayStore>()
	struct.array = placement.allocArray(size)
	LongArrayStore_init(struct.ptr, size)
	return struct.ptr
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