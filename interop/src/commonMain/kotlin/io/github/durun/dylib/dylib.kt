package io.github.durun.dylib

import io.github.durun.io.Closeable

expect class Dylib : Closeable {
	companion object {
		fun open(lib: String): Dylib
	}

	override fun close()
}

fun <T : Closeable, R> T.use(block: (T) -> R): R {
	val result = runCatching {
		block(this)
	}
	close()
	return result.getOrThrow()
}
