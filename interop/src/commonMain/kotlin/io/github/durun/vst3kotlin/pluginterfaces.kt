package io.github.durun.vst3kotlin

import io.github.durun.io.Closeable
import io.github.durun.path.Path
import io.github.durun.util.decodeAsBigEndian
import io.github.durun.util.encodeBigEndian

expect class Vst3Package : Closeable {
	companion object {
		fun open(vst3: Path): Vst3Package
	}

	override var isOpen: Boolean
		private set
	override fun close()
	fun openPluginFactory(): PluginFactory
}

expect class PluginFactory : FUnknown {
	override var isOpen: Boolean
		private set
	val factoryInfo: FactoryInfo
	val classInfo: List<ClassInfo>
	override fun close()
}

data class FactoryInfo(
	val vendor: String,
	val url: String,
	val email: String,
	val flags: Flags
) {
	data class Flags(
		val NoFlags: Boolean,
		val ClassesDiscardable: Boolean,
		val LicenseCheck: Boolean,
		val ComponentNonDiscardable: Boolean,
		val Unicode: Boolean
	)
}

data class TUID(
	private val bytes: ByteArray
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

data class ClassInfo(
	val classId: TUID,
	val cardinality: Int,
	val category: String,
	val name: String,
	val flags: Flags? = null,
	val subCategories: String? = null,
	val vendor: String? = null,
	val version: String? = null,
	val sdkVersion: String? = null
) {
	data class Flags(
		val value: Int
	)
}