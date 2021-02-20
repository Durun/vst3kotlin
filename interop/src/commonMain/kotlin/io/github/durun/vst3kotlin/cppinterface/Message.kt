package io.github.durun.vst3kotlin.cppinterface

import io.github.durun.util.ByteArrayReader
import io.github.durun.util.MessageBase
import io.github.durun.util.MessageDecoder
import io.github.durun.util.buildByteArray


sealed class Message(reader: ByteArrayReader) : MessageBase(reader) {

	@kotlin.ExperimentalUnsignedTypes
	class BeginEdit(reader: ByteArrayReader) : Message(reader) {
		override val type: Int = beginEdit_type
		val id: UInt by uInt()
		override fun toString(): String = "BeginEdit($id)"

		companion object {
			fun of(id: UInt): BeginEdit = decode(bytes(id))
			fun bytes(id: UInt): ByteArray = buildByteArray {
				appendInt(beginEdit_type)
				appendUInt(id)
			}
		}
	}

	@kotlin.ExperimentalUnsignedTypes
	class PerformEdit(reader: ByteArrayReader) : Message(reader) {
		override val type: Int = performEdit_type
		val id: UInt by uInt()
		val valueNormalized: Double by double()
		override fun toString(): String = "PerformEdit(id=$id, value=$valueNormalized)"

		companion object {
			fun of(id: UInt, valueNormalized: Double): PerformEdit = decode(bytes(id, valueNormalized))
			fun bytes(id: UInt, valueNormalized: Double): ByteArray = buildByteArray {
				appendInt(performEdit_type)
				appendUInt(id)
				appendDouble(valueNormalized)
			}
		}
	}

	@kotlin.ExperimentalUnsignedTypes
	class EndEdit(reader: ByteArrayReader) : Message(reader) {
		override val type: Int = endEdit_type
		val id: UInt by uInt()
		override fun toString(): String = "EndEdit($id)"

		companion object {
			fun of(id: UInt): EndEdit = decode(bytes(id))
			fun bytes(id: UInt): ByteArray = buildByteArray {
				appendInt(endEdit_type)
				appendUInt(id)
			}
		}
	}

	class RestartComponent(reader: ByteArrayReader) : Message(reader) {
		override val type: Int = endEdit_type
		val flags: Int by int()
		override fun toString(): String = "RestartComponent($flags)"

		companion object {
			fun of(flags: Int): RestartComponent = decode(bytes(flags))
			fun bytes(flags: Int): ByteArray = buildByteArray {
				appendInt(restartComponent_type)
				appendInt(flags)
			}
		}
	}

	class SetDirty(reader: ByteArrayReader) : Message(reader) {
		override val type: Int = setDirty_type
		val state: Boolean by bool()
		override fun toString(): String = "SetDirty($state)"

		companion object {
			fun of(state: Boolean): SetDirty = decode(bytes((state)))
			fun bytes(state: Boolean): ByteArray = buildByteArray {
				appendInt(setDirty_type)
				appendBool(state)
			}
		}
	}

	class RequestOpenEditor(reader: ByteArrayReader) : Message(reader) {
		override val type: Int = requestOpenEditor_type
		val name: String by utf8(nameLength)
		override fun toString(): String = "RequestOpenEditor($name)"

		companion object {
			const val nameLength = 256
			fun of(name: String): RequestOpenEditor = decode(bytes(name))
			fun bytes(name: String): ByteArray = buildByteArray {
				appendInt(requestOpenEditor_type)
				appendUTF8(name, nameLength)
			}
		}
	}

	class StartGroupEdit(reader: ByteArrayReader) : Message(reader) {
		override val type: Int = startGroupEdit_type
		override fun toString(): String = "StartGroupEdit()"

		companion object {
			val instance: StartGroupEdit = decode(bytes())
			fun bytes(): ByteArray = buildByteArray { appendInt(startGroupEdit_type) }
		}
	}

	class FinishGroupEdit(reader: ByteArrayReader) : Message(reader) {
		override val type: Int = finishGroupEdit_type
		override fun toString(): String = "FinishGroupEdit()"

		companion object {
			val instance: FinishGroupEdit = decode(bytes())
			fun bytes(): ByteArray = buildByteArray { appendInt(finishGroupEdit_type) }
		}
	}

	companion object : MessageDecoder {
		// type IDs
		private const val beginEdit_type: Int = 1
		private const val performEdit_type: Int = 2
		private const val endEdit_type: Int = 3
		private const val restartComponent_type: Int = 4
		private const val setDirty_type: Int = 5
		private const val requestOpenEditor_type: Int = 6
		private const val startGroupEdit_type: Int = 7
		private const val finishGroupEdit_type: Int = 8

		@kotlin.ExperimentalUnsignedTypes
		override fun <T : MessageBase> decode(reader: ByteArrayReader): T {
			val typeId = reader.readInt()
			val instance = when (typeId) {
				beginEdit_type -> BeginEdit(reader)
				performEdit_type -> PerformEdit(reader)
				endEdit_type -> EndEdit(reader)
				restartComponent_type -> RestartComponent(reader)
				setDirty_type -> SetDirty(reader)
				requestOpenEditor_type -> RequestOpenEditor(reader)
				startGroupEdit_type -> StartGroupEdit(reader)
				finishGroupEdit_type -> FinishGroupEdit(reader)
				else -> throw IllegalStateException("No type with typeId: $typeId")
			}
			//check(instance is T) { "Decode failed: $instance isn't ${T::class}" }
			return instance as T
		}
	}
}