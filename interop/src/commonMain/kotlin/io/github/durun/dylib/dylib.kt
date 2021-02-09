package io.github.durun.dylib

import io.github.durun.io.Closeable
import io.github.durun.path.Path

expect class Dylib : Closeable {
	companion object {
		fun open(lib: String): Dylib
		fun open(lib: Path): Dylib
	}

	override var isOpen: Boolean
		private set

	override fun close()
}

fun <T : Closeable, R> T.use(block: (T) -> R): R {
	val result = runCatching {
		block(this)
	}
	close()
	return result.getOrThrow()
}
