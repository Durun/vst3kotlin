package io.github.durun.vst3kotlin.base

import cwrapper.FUnknown_release
import io.github.durun.io.Closeable
import kotlinx.cinterop.CPointer
import kotlinx.cinterop.reinterpret

actual abstract class FUnknown(
	protected val thisRawPtr: CPointer<*>
) : Closeable {
	private val thisPtr get() = thisRawPtr.reinterpret<cwrapper.FUnknown>()
	actual final override var isOpen: Boolean = true
		private set

	actual override fun close() {
		check(isOpen)
		FUnknown_release(thisPtr)
		isOpen = false
	}
}