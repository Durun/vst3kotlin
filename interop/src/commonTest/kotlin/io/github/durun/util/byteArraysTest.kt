package io.github.durun.util

import io.kotest.matchers.shouldBe
import kotlin.test.Test

@OptIn(ExperimentalUnsignedTypes::class)
class ByteArraysTest {
	@Test
	fun buildByteArray() {
		val bytes = buildByteArray {
			appendUByte(114u)
			appendUInt(114514u)
			appendULong(1145141919810u)
			appendByte(-114)
			appendInt(-114514)
			appendLong(-1145141919810)
			appendBytes(byteArrayOf(-114, -51, -4, 19, -19, -8, 10))
			appendUBytes(ubyteArrayOf(114u, 51u, 4u, 19u, 19u, 8u, 10u))
			appendUTF8("Hello, world!", 13)
			appendFloat(-1.14514f)
			appendDouble(-1.145141919810)
			appendBool(true)
			appendBool(false)
		}

		bytes.readScope {
			readUByte() shouldBe (114u).toUByte()
			readUInt() shouldBe 114514u
			readULong() shouldBe 1145141919810u
			readByte() shouldBe (-114).toByte()
			readInt() shouldBe -114514
			readLong() shouldBe -1145141919810
			readBytes(7) shouldBe byteArrayOf(-114, -51, -4, 19, -19, -8, 10)
			readUBytes(7) shouldBe ubyteArrayOf(114u, 51u, 4u, 19u, 19u, 8u, 10u)
			readUtf8("Hello, world!".encodeToByteArray().size) shouldBe "Hello, world!"
			readFloat() shouldBe -1.14514f
			readDouble() shouldBe -1.145141919810
			readBool() shouldBe true
			readBool() shouldBe false
		}
	}
}