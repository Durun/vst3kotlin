package io.github.durun.vst3kotlin

import cwrapper.FUnknown_release
import io.github.durun.resource.Closeable
import io.github.durun.resource.use
import kotlinx.cinterop.CPointer
import kotlinx.cinterop.reinterpret

actual class VstInterface<P>(
	ptr: P
) : Closeable {
	private val thisPtr: CPointer<*>
	val ptr: CPointer<*> get() = thisPtr

	init {
		require(ptr is CPointer<*>)
		thisPtr = ptr
	}

	@Suppress("UNCHECKED_CAST")
	actual fun <R> usePointer(block: (P) -> R): R = use { block(it.thisPtr as P) }

	override var isOpen: Boolean = true
		private set

	override fun close() {
		check(isOpen)
		FUnknown_release(thisPtr.reinterpret())
		isOpen = false
	}
}