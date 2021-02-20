package io.github.durun.util

import kotlin.math.min


fun buildByteArray(block: ByteArrayBuilder.() -> Unit): ByteArray {
	return ByteArrayBuilder().apply(block).build()
}

class ByteArrayBuilder {
	val bytes: MutableList<Byte> = mutableListOf()
	fun build(): ByteArray = bytes.toByteArray()

	fun appendByte(v: Byte) = bytes.add(v)
	fun appendBytes(v: ByteArray) = v.forEach { bytes.add(it) }
	fun appendUTF8(v: String, sizeByte: Int) {
		val b = v.encodeToByteArray()
		val padding = sizeByte - b.size
		check(0 <= padding)
		appendBytes(b)
		if (0 < padding) appendBytes(ByteArray(padding))
	}

	fun appendInt(v: Int) = bytes.addAll(v.toBytes())
	fun appendLong(v: Long) = bytes.addAll(v.toBytes())
	fun appendFloat(v: Float) = bytes.addAll(v.toBytes().also { println(it.joinToString()) })
	fun appendDouble(v: Double) = bytes.addAll(v.toBytes())


	@OptIn(ExperimentalUnsignedTypes::class)
	fun appendUByte(v: UByte) = appendByte(v.toByte())

	@OptIn(ExperimentalUnsignedTypes::class)
	fun appendUBytes(v: UByteArray) {
		v.forEach { bytes.add(it.toByte()) }
	}

	@OptIn(ExperimentalUnsignedTypes::class)
	fun appendUInt(v: UInt) {
		bytes.addAll(v.toBytes())
	}

	@OptIn(ExperimentalUnsignedTypes::class)
	fun appendULong(v: ULong) {
		bytes.addAll(v.toBytes())
	}
}

private fun Int.toBytes(): List<Byte> = this.toUInt().toBytes()
private fun Long.toBytes(): List<Byte> = this.toULong().toBytes()
private fun Float.toBytes(): List<Byte> = this.toRawBits().toBytes()
private fun Double.toBytes(): List<Byte> = this.toRawBits().toBytes()

@OptIn(ExperimentalUnsignedTypes::class)
private fun UInt.toBytes(): List<Byte> {
	return listOf(
		(this shr 0).toByte(),
		(this shr 8).toByte(),
		(this shr 16).toByte(),
		(this shr 24).toByte()
	)
}

@OptIn(ExperimentalUnsignedTypes::class)
private fun ULong.toBytes(): List<Byte> {
	return listOf(
		(this shr 0).toByte(),
		(this shr 8).toByte(),
		(this shr 16).toByte(),
		(this shr 24).toByte(),
		(this shr 32).toByte(),
		(this shr 40).toByte(),
		(this shr 48).toByte(),
		(this shr 56).toByte()
	)
}

fun <R> ByteArray.readScope(block: ByteArrayReader.() -> R): R {
	return ByteArrayReader(this).block()
}

@OptIn(ExperimentalUnsignedTypes::class)
class ByteArrayReader(private val buf: ByteArray) {
	var offset: Int = 0
		private set

	fun readByte(): Byte {
		val b = buf[offset]
		offset++
		return b
	}

	fun readBytes(size: Int): ByteArray {
		val endExclusive = min(offset + size, buf.size)
		val b = buf.sliceArray(offset until endExclusive)
		offset += size
		return b
	}

	fun readUtf8(sizeByte: Int): String = readBytes(sizeByte).decodeToString()

	fun readUByte(): UByte = readByte().toUByte()
	fun readUBytes(size: Int): UByteArray = readBytes(size).toUByteArray()
	fun readUInt(): UInt = readUIntAt(buf, offset).also { offset += 4 }
	fun readULong(): ULong = readULongAt(buf, offset).also { offset += 8 }

	fun readInt(): Int = readUInt().toInt()
	fun readLong(): Long = readULong().toLong()
	fun readFloat(): Float = Float.fromBits(readInt())
	fun readDouble(): Double = Double.fromBits(readLong())

	companion object {
		fun readUIntAt(bytes: ByteArray, offsetByte: Int): UInt {
			val b0 = bytes[offsetByte + 0].toUByte().toUInt()
			val b1 = bytes[offsetByte + 1].toUByte().toUInt()
			val b2 = bytes[offsetByte + 2].toUByte().toUInt()
			val b3 = bytes[offsetByte + 3].toUByte().toUInt()
			return (b3 shl 24) or (b2 shl 16) or (b1 shl 8) or (b0 shl 0)
		}

		fun readULongAt(bytes: ByteArray, offsetByte: Int): ULong {
			val b0 = bytes[offsetByte + 0].toUByte().toULong()
			val b1 = bytes[offsetByte + 1].toUByte().toULong()
			val b2 = bytes[offsetByte + 2].toUByte().toULong()
			val b3 = bytes[offsetByte + 3].toUByte().toULong()
			val b4 = bytes[offsetByte + 4].toUByte().toULong()
			val b5 = bytes[offsetByte + 5].toUByte().toULong()
			val b6 = bytes[offsetByte + 6].toUByte().toULong()
			val b7 = bytes[offsetByte + 7].toUByte().toULong()
			return (b7 shl 56) or (b6 shl 48) or (b5 shl 40) or (b4 shl 32) or (b3 shl 24) or (b2 shl 16) or (b1 shl 8) or (b0 shl 0)
		}

		fun readIntAt(bytes: ByteArray, offsetByte: Int): Int = readUIntAt(bytes, offsetByte).toInt()
		fun readLongAt(bytes: ByteArray, offsetByte: Int): Long = readULongAt(bytes, offsetByte).toLong()
	}
}