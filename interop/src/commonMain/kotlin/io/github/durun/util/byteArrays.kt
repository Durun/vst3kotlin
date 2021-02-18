package io.github.durun.util


fun buildByteArray(block: ByteArrayBuilder.() -> Unit): ByteArray {
	return ByteArrayBuilder().apply(block).build()
}

class ByteArrayBuilder {
	val bytes: MutableList<Byte> = mutableListOf()
	fun build(): ByteArray = bytes.toByteArray()

	fun appendByte(v: Byte) {
		bytes.add(v)
	}

	fun appendInt(v: Int) {
		bytes.addAll(v.toBytes())
	}

	fun appendLong(v: Long) {
		bytes.addAll(v.toBytes())
	}

	@OptIn(ExperimentalUnsignedTypes::class)
	fun appendUByte(v: UByte) = appendByte(v.toByte())

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

class ByteArrayReader(private val buf: ByteArray) {
	var offset = 0

	fun readByte(): Byte {
		val b = buf[offset]
		offset++
		return b
	}

	fun readInt(): Int = readUInt().toInt()
	fun readLong(): Long = readULong().toLong()
	fun readUByte(): UByte = readByte().toUByte()

	@OptIn(ExperimentalUnsignedTypes::class)
	fun readUInt(): UInt {
		val b0 = buf[offset + 0].toUByte().toUInt()
		val b1 = buf[offset + 1].toUByte().toUInt()
		val b2 = buf[offset + 2].toUByte().toUInt()
		val b3 = buf[offset + 3].toUByte().toUInt()
		offset += 4
		return (b3 shl 24) or (b2 shl 16) or (b1 shl 8) or (b0 shl 0)
	}

	@OptIn(ExperimentalUnsignedTypes::class)
	fun readULong(): ULong {
		val b0 = buf[offset + 0].toUByte().toULong()
		val b1 = buf[offset + 1].toUByte().toULong()
		val b2 = buf[offset + 2].toUByte().toULong()
		val b3 = buf[offset + 3].toUByte().toULong()
		val b4 = buf[offset + 4].toUByte().toULong()
		val b5 = buf[offset + 5].toUByte().toULong()
		val b6 = buf[offset + 6].toUByte().toULong()
		val b7 = buf[offset + 7].toUByte().toULong()
		offset += 8
		return (b7 shl 56) or (b6 shl 48) or (b5 shl 40) or (b4 shl 32) or (b3 shl 24) or (b2 shl 16) or (b1 shl 8) or (b0 shl 0)
	}
}