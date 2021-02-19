package io.github.durun.vst3kotlin.base

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

		companion object {
			fun of(id: UInt): BeginEdit = BeginEdit(ByteArrayReader(bytesOf(id)))
			fun bytesOf(id: UInt): ByteArray = buildByteArray {
				appendUInt(id)
			}
		}
	}

	companion object {
		// type IDs
		val typeBeginEdit: Int = 1

		inline fun <reified T : MessageBase> decode(reader: ByteArrayReader): T {
			val typeId = reader.readInt()
			val instance = when (typeId) {
				typeBeginEdit -> BeginEdit(reader)
				else -> throw IllegalStateException("No type with typeId: $typeId")
			}
			check(instance is T) { "Decode failed: $instance isn't ${T::class}" }
			return instance
		}

		@OptIn(ExperimentalStdlibApi::class)
		inline fun <reified T : MessageBase> decodeAll(reader: ByteArrayReader, sizeByte: Int): List<T> {
			return buildList {
				while (reader.offset < sizeByte) add(decode(reader))
			}
		}
	}
}