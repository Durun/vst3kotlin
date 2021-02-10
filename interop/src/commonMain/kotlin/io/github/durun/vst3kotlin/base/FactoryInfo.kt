package io.github.durun.vst3kotlin.base

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