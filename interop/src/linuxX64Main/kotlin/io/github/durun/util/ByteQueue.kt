package io.github.durun.util

import cwrapper.ByteQueue
import cwrapper.ByteQueueLength
import cwrapper.ByteQueue_dequeue
import cwrapper.ByteQueue_enqueue
import kotlinx.cinterop.*

fun allocByteQueue(placement: NativeFreeablePlacement): CPointer<ByteQueue> {
	return placement.allocArray(ByteQueueLength)
}

fun CPointer<ByteQueue>.enqueue(bytes: ByteArray) {
	memScoped {
		ByteQueue_enqueue(this@enqueue, bytes.toCValues().ptr, bytes.size)
	}
}

fun CPointer<ByteQueue>.dequeue(maxSize: Int): ByteArray {
	return memScoped {
		val buf = allocArray<ByteVar>(maxSize)
		val read = ByteQueue_dequeue(this@dequeue, buf, maxSize)
		buf.readBytes(read)
	}
}
