package io.github.durun.vst3kotlin.base

import io.github.durun.util.decodeAsBigEndian
import io.github.durun.util.encodeBigEndian

data class TUID(
	val bytes: ByteArray
) {
	constructor(id: String) : this(id.decodeAsBigEndian())

	init {
		require(bytes.size == 16) { "TUID must be 16 bytes but was ${bytes.size} bytes" }
	}

	override fun toString(): String = bytes.encodeBigEndian()

	override fun equals(other: Any?): Boolean {
		if (this === other) return true
		if (other == null || this::class != other::class) return false

		other as TUID

		if (!bytes.contentEquals(other.bytes)) return false

		return true
	}

	override fun hashCode(): Int {
		return bytes.contentHashCode()
	}
}