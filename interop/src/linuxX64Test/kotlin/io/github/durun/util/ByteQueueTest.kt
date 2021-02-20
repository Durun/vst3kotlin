package io.github.durun.util

import kotlinx.cinterop.memScoped
import kotlin.test.Test

class ByteQueueTest {
	val testData = byteArrayOf(1, 2, 3, 4, 5)

	@Test
	fun enqueue() {
		memScoped {
			val queue = allocByteQueue()

			queue.enqueue(testData)
			val read = queue.dequeue(5)

			queue.free()
			println(read)
		}
	}
}