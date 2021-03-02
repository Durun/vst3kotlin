package io.github.durun.vst3kotlin.pluginterface.base

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
	) {
		constructor(flags: Int) : this(
			flags == 0,
			flags and (1 shl 0) != 0,
			flags and (1 shl 1) != 0,
			flags and (1 shl 2) != 0,
			flags and (1 shl 3) != 0
		)
	}
}