package io.github.durun.util

private val hexTable = arrayOf('0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F')

fun Byte.encodeHex(): String {
	val i = this.toUInt()
	val lo = (i % 16u).toInt()
	val hi = ((i / 16u) % 16u).toInt()
	return "${hexTable[hi]}${hexTable[lo]}"
}

fun ByteArray.encodeBigEndian(): String = this.joinToString("") { it.encodeHex() }
fun ByteArray.encodeLittleEndian(): String = this.reversedArray().encodeBigEndian()

fun Char.decodeAsHEX(): Int {
	return if (this in '0'..'9') this - '0'
	else {
		require(this in 'A'..'F') {"Out of HEX number: '$this'"}
		this - 'A' + 10
	}
}

fun String.decodeAsBigEndian(): ByteArray {
	require(this.length % 2 == 0)
	return this.chunkedSequence(2) {
		val hi = it[0].toUpperCase()
		val lo = it[1].toUpperCase()
		(hi.decodeAsHEX() * 16 + lo.decodeAsHEX()).toByte()
	}.toList().toByteArray()
}

fun String.decodeAsLittleEndian(): ByteArray = this.decodeAsBigEndian().reversedArray()