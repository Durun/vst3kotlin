package io.github.durun.vst3kotlin

import cwrapper.GlobalStore_read
import cwrapper.GlobalStore_write
import kotlinx.cinterop.COpaquePointer

object GlobalMem {
	operator fun get(index: Int): COpaquePointer? = GlobalStore_read(index)
	operator fun set(index: Int, value: COpaquePointer?) = GlobalStore_write(index, value)

	// reserved area
	var hostContext: COpaquePointer?
		get() = get(1)
		set(value) = set(1, value)
}
