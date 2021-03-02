package io.github.durun.vst3kotlin.cppinterface

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

	@Test
	fun enqueueViaCFunction() {
		val enqueue = staticCFunction<Unit> {
			memScoped {
				val data = "Hello, world!".encodeToByteArray()
				MessageQueue_enqueue(data.toCValues().ptr, data.size)
			}
		}

		enqueue()
		enqueue()

		val read = memScoped {
			val bufSize = 256
			val buf = allocArray<ByteVar>(bufSize)
			val read = MessageQueue_dequeue(buf, bufSize)
			buf.readBytes(read)
		}
		println(read.decodeToString())
		read.decodeToString() shouldBe "Hello, world!Hello, world!"
	}
}
