package io.github.durun.util

import kotlin.properties.ReadWriteProperty
import kotlin.reflect.KProperty


sealed class Data<T>(var value: T) {
	class BYTE(value: Byte) : Data<Byte>(value)
	class BYTES(value: ByteArray) : Data<ByteArray>(value)
	class INT(value: Int) : Data<Int>(value)
	class LONG(value: Long) : Data<Long>(value)
	class FLOAT(value: Float) : Data<Float>(value)
	class DOUBLE(value: Double) : Data<Double>(value)
	class UTF8(value: String, val sizeByte: Int) : Data<String>(value)
	class UBYTE @OptIn(ExperimentalUnsignedTypes::class) constructor(value: UByte) : Data<UByte>(value)
	class UBYTES @OptIn(ExperimentalUnsignedTypes::class) constructor(value: UByteArray) : Data<UByteArray>(value)
	class UINT @OptIn(ExperimentalUnsignedTypes::class) constructor(value: UInt) : Data<UInt>(value)
	class ULONG @OptIn(ExperimentalUnsignedTypes::class) constructor(value: ULong) : Data<ULong>(value)
}

class DataProperty<T>(val data: Data<T>) : ReadWriteProperty<MessageBase, T> {
	override fun getValue(thisRef: MessageBase, property: KProperty<*>): T = data.value
	override fun setValue(thisRef: MessageBase, property: KProperty<*>, value: T) {
		data.value = value
	}
}
class StringDataProperty(val data: Data<String>) : ReadWriteProperty<MessageBase, String> {
	override fun getValue(thisRef: MessageBase, property: KProperty<*>): String = data.value.trimEnd((0).toChar())
	override fun setValue(thisRef: MessageBase, property: KProperty<*>, value: String) {
		data.value = value
	}
}

abstract class MessageBase(
	private val reader: ByteArrayReader
) {
	protected abstract val type: Int
	private var actualType: Int? = null

	fun encode(): ByteArray {
		return buildByteArray {
			appendInt(type)
			datas.forEach {
				when (it) {
					is Data.BYTE -> appendByte(it.value)
					is Data.BYTES -> appendBytes(it.value)
					is Data.INT -> appendInt(it.value)
					is Data.LONG -> appendLong(it.value)
					is Data.FLOAT -> appendFloat(it.value)
					is Data.DOUBLE -> appendDouble(it.value)
					is Data.UTF8 -> appendUTF8(it.value, it.sizeByte)
					is Data.UBYTE -> appendUByte(it.value)
					is Data.UBYTES -> appendUBytes(it.value)
					is Data.UINT -> appendUInt(it.value)
					is Data.ULONG -> appendULong(it.value)
				}
			}
		}
	}

	// register methods
	protected fun byte(): ReadWriteProperty<MessageBase, Byte> {
		val value = reader.readByte()
		val data = Data.BYTE(value)
		datas.add(data)
		debug(data)
		return DataProperty(data)
	}

	protected fun byteArray(size: Int): ReadWriteProperty<MessageBase, ByteArray> {
		val value = reader.readBytes(size)
		val data = Data.BYTES(value)
		datas.add(data)
		debug(data)
		return DataProperty(data)
	}

	protected fun int(): ReadWriteProperty<MessageBase, Int> {
		val value = reader.readInt()
		val data = Data.INT(value)
		datas.add(data)
		debug(data)
		return DataProperty(data)
	}

	protected fun long(): ReadWriteProperty<MessageBase, Long> {
		val value = reader.readLong()
		val data = Data.LONG(value)
		datas.add(data)
		debug(data)
		return DataProperty(data)
	}

	protected fun float(): ReadWriteProperty<MessageBase, Float> {
		val value = reader.readFloat()
		val data = Data.FLOAT(value)
		datas.add(data)
		debug(data)
		return DataProperty(data)
	}

	protected fun double(): ReadWriteProperty<MessageBase, Double> {
		val value = reader.readDouble()
		val data = Data.DOUBLE(value)
		datas.add(data)
		debug(data)
		return DataProperty(data)
	}

	protected fun utf8(sizeByte: Int): ReadWriteProperty<MessageBase, String> {
		val value = reader.readUtf8(sizeByte)
		val data = Data.UTF8(value, sizeByte)
		datas.add(data)
		debug(data)
		return StringDataProperty(data)
	}

	@OptIn(ExperimentalUnsignedTypes::class)
	protected fun uInt(): ReadWriteProperty<MessageBase, UInt> {
		val value = reader.readUInt()
		val data = Data.UINT(value)
		datas.add(data)
		debug(data)
		return DataProperty(data)
	}

	@OptIn(ExperimentalUnsignedTypes::class)
	protected fun uLong(): ReadWriteProperty<MessageBase, ULong> {
		val value = reader.readULong()
		val data = Data.ULONG(value)
		datas.add(data)
		debug(data)
		return DataProperty(data)
	}

	private val datas: MutableList<Data<*>> = mutableListOf()
	private fun debug(v:Data<*>) {
		//println("offset=${reader.offset} : ${v.value}")
	}
}
