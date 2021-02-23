package io.github.durun.util

import cwrapper.ByteQueueLength
import io.kotest.matchers.shouldBe
import kotlinx.cinterop.memScoped
import kotlinx.cinterop.nativeHeap
import kotlin.test.Test

class ByteQueueTest {
	val testData = byteArrayOf(1, 2, 3, 4, 5)

	@Test
	fun onHeap() {
		val queue = allocByteQueue(nativeHeap)

		queue.enqueue(testData)
		val read = queue.dequeue(ByteQueueLength)

		queue.free()

		read shouldBe testData
	}

	@Test
	fun onStack() {
		val read = memScoped {
			val queue = allocByteQueue(this)

			queue.enqueue(testData)
			queue.dequeue(ByteQueueLength)
		}
		read shouldBe testData
	}
}