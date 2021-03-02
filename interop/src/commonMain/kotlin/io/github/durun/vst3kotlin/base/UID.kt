package io.github.durun.vst3kotlin.base

import io.github.durun.data.decodeAsBigEndian
import io.github.durun.data.encodeBigEndian

data class UID(
	val bytes: ByteArray	// big endian
) {
	constructor(id: String) : this(id.decodeAsBigEndian())

	init {
		require(bytes.size == 16) { "UID must be 16 bytes but was ${bytes.size} bytes" }
	}

	override fun toString(): String = bytes.encodeBigEndian()

	override fun equals(other: Any?): Boolean {
		if (this === other) return true
		if (other == null || this::class != other::class) return false

		other as UID

		if (!bytes.contentEquals(other.bytes)) return false

		return true
	}

	override fun hashCode(): Int {
		return bytes.contentHashCode()
	}
}