package io.github.durun.util

import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty


sealed class Data<T>(var value: T) {
	class BYTE(value: Byte) : Data<Byte>(value)
	class INT(value: Int) : Data<Int>(value)
	class LONG(value: Long) : Data<Long>(value)
	class UBYTE @OptIn(ExperimentalUnsignedTypes::class) constructor(value: UByte) : Data<UByte>(value)
	class UINT @OptIn(ExperimentalUnsignedTypes::class) constructor(value: UInt) : Data<UInt>(value)
	class ULONG @OptIn(ExperimentalUnsignedTypes::class) constructor(value: ULong) : Data<ULong>(value)
}

class DataProperty<T>(val data: Data<T>) : ReadWriteProperty<MessageBase, T> {
	override fun getValue(thisRef: MessageBase, property: KProperty<*>): T = data.value
	override fun setValue(thisRef: MessageBase, property: KProperty<*>, value: T) {
		data.value = value
	}
}

abstract class MessageBase(bytes: ByteArray) {
	protected abstract val type: Int
	fun encode(): ByteArray {
		return buildByteArray {
			appendInt(type)
			datas.forEach {
				when (it) {
					is Data.BYTE -> appendByte(it.value)
					is Data.INT -> appendInt(it.value)
					is Data.LONG -> appendLong(it.value)
					is Data.UBYTE -> appendUByte(it.value)
					is Data.UINT -> appendUInt(it.value)
					is Data.ULONG -> appendULong(it.value)
				}
			}
		}
	}

	// register methods
	protected fun int(bytes: ByteArray): ReadWriteProperty<MessageBase, Int> {
		val value = getReader(bytes).readInt()
		val data = Data.INT(value)
		datas.add(data)
		return DataProperty(data)
	}

	protected fun long(bytes: ByteArray): ReadWriteProperty<MessageBase, Long> {
		val value = getReader(bytes).readLong()
		val data = Data.LONG(value)
		datas.add(data)
		return DataProperty(data)
	}

	@OptIn(ExperimentalUnsignedTypes::class)
	protected fun uInt(bytes: ByteArray): ReadWriteProperty<MessageBase, UInt> {
		val value = getReader(bytes).readUInt()
		val data = Data.UINT(value)
		datas.add(data)
		return DataProperty(data)
	}

	@OptIn(ExperimentalUnsignedTypes::class)
	protected fun uLong(bytes: ByteArray): ReadWriteProperty<MessageBase, ULong> {
		val value = getReader(bytes).readULong()
		val data = Data.ULONG(value)
		datas.add(data)
		return DataProperty(data)
	}

	companion object {
		protected fun buildMessageBytes(type: Int, block: ByteArrayBuilder.() -> Unit): ByteArray {
			return buildByteArray {
				appendInt(type)
				block()
			}
		}
	}

	private val datas: MutableList<Data<*>> = mutableListOf()
	private var reader: ByteArrayReader? = null
	private fun getReader(bytes: ByteArray): ByteArrayReader {
		return reader ?: ByteArrayReader(bytes).also {
			val type = it.readInt()    // skip type specifier
			reader = it
		}
	}
}
