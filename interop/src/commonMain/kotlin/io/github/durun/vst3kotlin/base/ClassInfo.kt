package io.github.durun.vst3kotlin.base

data class ClassInfo(
	val classId: UID,
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