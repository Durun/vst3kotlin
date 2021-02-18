package io.github.durun.util

import io.kotest.matchers.shouldBe
import kotlin.test.Test

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
		}

		bytes.readScope {
			readUByte() shouldBe (114u).toUByte()
			readUInt() shouldBe 114514u
			readULong() shouldBe 1145141919810u
			readByte() shouldBe (-114).toByte()
			readInt() shouldBe -114514
			readLong() shouldBe -1145141919810
		}
	}
}