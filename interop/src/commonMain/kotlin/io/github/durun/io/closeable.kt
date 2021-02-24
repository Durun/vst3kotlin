package io.github.durun.io

interface Closeable {
	/**
	 * Not thread-safe
	 */
	val isOpen: Boolean
	fun close()
}

fun <T : Closeable, R> T.use(block: (T) -> R): R {
	val result = runCatching {
		block(this)
	}
	close()
	return result.getOrThrow()
}