package io.github.durun.util

import io.kotest.matchers.shouldBe
import kotlin.test.Test

class messageBaseTest {
	@Test
	fun encodeAndDecode() {
		val messages: List<Message> = listOf(
			Message.A.of(114, "Hello!", "abcde"),
			Message.B.of(-114, -5141919810, 114u, 5141919810u),
			Message.A.of(514, "Hello, world!", "ABCDE")
		).also { println(it.joinToString()) }

		val encodeds = messages.map { it.encode() }
			.onEach { println(it.joinToString()) }

		val decodeds: List<Message> = encodeds.map { Message.decode(it) }
		println(decodeds.joinToString())

		messages.zip(encodeds).zip(decodeds).forEach { (it, decoded) ->
			val (message, encoded) = it
			encoded shouldBe message.bytes
			decoded.toString() shouldBe message.toString()
		}
	}

	sealed class Message(
		val bytes: ByteArray // for debug
	) : MessageBase(bytes) {

		class A(bytes: ByteArray) : Message(bytes) {
			val id: Int by int(bytes)
			val text1: String by utf8(bytes, text1Size)
			val text2: ByteArray by byteArray(bytes, "ABCDE".encodeToByteArray().size)
			override val type: Int = typeA
			override fun toString(): String = "MessageA(id=$id, text1=$text1, text2=${text2.decodeToString()})"

			companion object {
				private val text1Size = 16
				fun of(id: Int, text1: String, text2: String): A = A(buildMessageBytes(typeA) {
					appendInt(id)
					require(text1.encodeToByteArray().size <= text1Size)
					appendUTF8(text1, 16)
					appendBytes(text2.encodeToByteArray())
				})
			}
		}

		class B(bytes: ByteArray) : Message(bytes) {
			val x: Int by int(bytes)
			val y: Long by long(bytes)
			val z: UInt by uInt(bytes)
			val w: ULong by uLong(bytes)
			override val type: Int = typeB
			override fun toString(): String = "MessageB($x, $y, $z, $w)"

			companion object {
				fun of(x: Int, y: Long, z: UInt, w: ULong): B = B(buildMessageBytes(typeB) {
					appendInt(x)
					appendLong(y)
					appendUInt(z)
					appendULong(w)
				})
			}
		}

		companion object {
			// type IDs
			val typeA: Int = 0
			val typeB: Int = -1

			inline fun <reified T : MessageBase> decode(bytes: ByteArray): T {
				val typeId = ByteArrayReader.readIntAt(bytes, 0)
				val instance = when (typeId) {
					typeA -> A(bytes)
					typeB -> B(bytes)
					else -> throw IllegalStateException("No type with typeId: $typeId")
				}
				check(instance is T) { "Decode failed: $instance isn't ${T::class}" }
				return instance
			}
		}
	}
}