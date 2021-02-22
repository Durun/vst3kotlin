package io.github.durun.vst3kotlin.base

import cwrapper.*

val Int.kResultString: String
	get() = when (this) {
		kResultOk -> "kResultOK"
		kResultTrue -> "kResultTrue"
		kResultFalse -> "kResultFalse"
		kNoInterface -> "kNoInterface"
		kInvalidArgument -> "kInvalidArgument"
		kNotImplemented -> "kNotImplemented"
		kInternalError -> "kInternalError"
		kNotInitialized -> "kNotInitialized"
		kOutOfMemory -> "kOutOfMemory"
		else -> "unknown"
	}