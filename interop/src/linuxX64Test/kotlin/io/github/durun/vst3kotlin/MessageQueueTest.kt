package io.github.durun.vst3kotlin

import cwrapper.MessageQueue_dequeue
import cwrapper.MessageQueue_enqueue
import io.kotest.matchers.shouldBe
import kotlinx.cinterop.*
import kotlin.test.Test

class MessageQueueTest {
	@Test
	fun messageQueue() {
		val data = "Hello, world!".encodeToByteArray()
		memScoped {
			MessageQueue_enqueue(data.toCValues().ptr, data.size)
		}

		val read = memScoped {
			val buf = allocArray<ByteVar>(data.size)
			MessageQueue_dequeue(buf, data.size)
			buf.readBytes(data.size)
		}
		println(read.decodeToString())
		read shouldBe data
	}
}
