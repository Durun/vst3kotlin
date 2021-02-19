package io.github.durun.vst3kotlin.base

import io.github.durun.util.ByteArrayBuilder
import io.github.durun.util.ByteArrayReader
import io.github.durun.util.MessageBase
import io.github.durun.util.buildByteArray

expect class HostContext : FUnknown {
	fun receiveMessages(): List<Message>
}

sealed class Message(reader: ByteArrayReader) : MessageBase(reader) {
	class BeginEdit(reader: ByteArrayReader) : Message(reader) {
		override val type: Int = typeBeginEdit
		val id: UInt by uInt()
		override fun toString(): String = "BeginEdit(id=$id)"

		companion object {
			private fun ByteArrayBuilder.bodyBytes(id: UInt) {
				appendUInt(id)
			}
			fun bytesOf(id: UInt) :ByteArray = buildByteArray{
				appendInt(typeBeginEdit)
				bodyBytes(id)
			}
			fun of(id: UInt): BeginEdit {
				return BeginEdit(ByteArrayReader(ByteArrayBuilder().apply { bodyBytes(id) }.build()))
			}
		}
	}
	class EndEdit(reader: ByteArrayReader) : Message(reader) {
		override val type: Int = typeEndEdit
		val id: UInt by uInt()
		override fun toString(): String = "EndEdit(id=$id)"

		companion object {
			private fun ByteArrayBuilder.bodyBytes(id: UInt) {
				appendUInt(id)
			}
			fun bytesOf(id: UInt) :ByteArray = buildByteArray{
				appendInt(typeEndEdit)
				bodyBytes(id)
			}
			fun of(id: UInt): BeginEdit {
				return BeginEdit(ByteArrayReader(ByteArrayBuilder().apply { bodyBytes(id) }.build()))
			}
		}
	}

	companion object {
		// type IDs
		val typeBeginEdit: Int = 1
		val typeEndEdit: Int = 2
		val typePerformEdit: Int = 3
		val typeRestartComponent: Int = 4
		val typeSetDirty: Int = 5
		val typeRequestOpenEditor: Int = 6
		val typeStartGroupEdit: Int = 7
		val typeFinishGroupEdit: Int = 8

		inline fun <reified T : MessageBase> decode(reader: ByteArrayReader): T {
			val typeId = reader.readInt()
			val instance = when (typeId) {
				typeBeginEdit -> BeginEdit(reader)
				typeEndEdit -> EndEdit(reader)
				else -> throw IllegalStateException("No type with typeId: $typeId")
			}
			check(instance is T) { "Decode failed: $instance isn't ${T::class}" }
			return instance
		}

		@OptIn(ExperimentalStdlibApi::class)
		inline fun <reified T : MessageBase> decodeAll(reader: ByteArrayReader, sizeByte: Int): List<T> {
			return buildList {
				while (reader.offset < sizeByte) {
					add(decode(reader))
				}
			}
		}
	}
}