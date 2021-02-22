package io.github.durun.util

import cwrapper.*
import kotlinx.cinterop.*

fun allocByteQueue(placement: NativePlacement): CPointer<ByteQueue> {
	val struct = placement.alloc<ByteQueue>()
	struct.array = placement.allocArray(ByteQueueLength)
	ByteQueue_init(struct.ptr)
	return struct.ptr
}

fun CPointer<ByteQueue>.free() {
	ByteQueue_free(this)
}

fun CPointer<ByteQueue>.enqueue(bytes: ByteArray) {
	memScoped {
		val ptr = bytes.toCValues().ptr
		ByteQueue_enqueue(this@enqueue, ptr, bytes.size)
	}
}

fun CPointer<ByteQueue>.dequeue(maxSize: Int): ByteArray {
	return memScoped {
		val buf = allocArray<ByteVar>(maxSize)
		val read = ByteQueue_dequeue(this@dequeue, buf, maxSize)
		buf.readBytes(read)
	}
}
