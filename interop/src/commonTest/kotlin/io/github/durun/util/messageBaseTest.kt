package io.github.durun.util

import io.github.durun.data.ByteArrayReader
import io.github.durun.data.buildByteArray
import io.kotest.matchers.shouldBe
import kotlin.test.Test

class messageBaseTest {
	@Test
	fun encodeAndDecode() {
		val messages: List<Message> = sequence {
			yield(Message.A.of(114, "Hello!", "abcde"))
			yield(Message.B.of(-114, -5141919810, 114u, 5141919810u, -1.14f, 5.141919810))
			yield(Message.A.of(514, "Helloです", "ABCDE"))
		}.onEach { println(it) }.toList()

		val encodeds = messages.map { it.encode() }
			.onEach { println(it.joinToString()) }

		val decodeds: List<Message> = encodeds.map { Message.decode(ByteArrayReader(it)) }
		decodeds.onEach { println(it) }

		messages.zip(decodeds).forEach { (message, decoded) ->
			decoded.toString() shouldBe message.toString()
		}
	}

	sealed class Message(reader: ByteArrayReader) : MessageBase(reader) {

		class A(reader: ByteArrayReader) : Message(reader) {
			val id: Int by int()
			val text1: String by utf8(text1Size)
			val text2: ByteArray by byteArray("ABCDE".encodeToByteArray().size)
			override val type: Int = typeA
			override fun toString(): String = "MessageA(id=$id, text1=$text1, text2=${text2.decodeToString()})"

			companion object {
				private val text1Size = 16
				fun of(id: Int, text1: String, text2: String): A = A(buildByteArray {
					appendInt(id)
					require(text1.encodeToByteArray().size <= text1Size)
					appendUTF8(text1, 16)
					appendBytes(text2.encodeToByteArray())
				}.let { ByteArrayReader(it) })
			}
		}

		class B(reader: ByteArrayReader) : Message(reader) {
			val x: Int by int()
			val y: Long by long()
			val z: UInt by uInt()
			val w: ULong by uLong()
			val a: Float by float()
			val b: Double by double()
			override val type: Int = typeB
			override fun toString(): String = "MessageB($x, $y, $z, $w, $a, $b)"

			companion object {
				fun of(x: Int, y: Long, z: UInt, w: ULong, a: Float, b: Double): B = B(buildByteArray {
					appendInt(x)
					appendLong(y)
					appendUInt(z)
					appendULong(w)
					appendFloat(a)
					appendDouble(b)
				}.let { ByteArrayReader((it)) })
			}
		}

		companion object {
			// type IDs
			val typeA: Int = 4
			val typeB: Int = -5

			inline fun <reified T : MessageBase> decode(reader: ByteArrayReader): T {
				val typeId = reader.readInt()
				val instance = when (typeId) {
					typeA -> A(reader)
					typeB -> B(reader)
					else -> throw IllegalStateException("No type with typeId: $typeId")
				}
				check(instance is T) { "Decode failed: $instance isn't ${T::class}" }
				return instance
			}
		}
	}
}