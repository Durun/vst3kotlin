package io.github.durun.vst3kotlin

import io.github.durun.resource.Closeable

expect class VstInterface<P> : Closeable {
	fun <R> usePointer(block: (P) -> R): R
}