package io.github.durun.vst3kotlin

import io.github.durun.io.Closeable

expect class VstInterface<P> : Closeable {
	fun <R> usePointer(block: (P) -> R): R
}