package io.github.durun.data

import io.kotest.matchers.shouldBe
import kotlin.test.Test

class BytesTest {
	@Test
	fun encodeHex() {
		0x00.toByte().encodeHex() shouldBe "00"
		0x20.toByte().encodeHex() shouldBe "20"
		0xFF.toByte().encodeHex() shouldBe "FF"
	}

	@Test
	fun encodeBigEndian() {
		listOf(0x01, 0x23, 0x45, 0x67).map { it.toByte() }.toByteArray().encodeBigEndian() shouldBe "01234567"
	}

	@Test
	fun encodeLittleEndian() {
		listOf(0x01, 0x23, 0x45).map { it.toByte() }.toByteArray().encodeLittleEndian() shouldBe "452301"
	}

	@Test
	fun decodeBigEndian() {
		"01234567".decodeAsBigEndian() shouldBe listOf(0x01, 0x23, 0x45, 0x67).map { it.toByte() }.toByteArray()
	}

	@Test
	fun decodeLittleEndian() {
		"452301".decodeAsLittleEndian() shouldBe listOf(0x01, 0x23, 0x45).map { it.toByte() }.toByteArray()
	}
}