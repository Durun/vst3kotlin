package io.github.durun.io

/**
 * [Closeable] is not thread-safe.
 * If you want to manage thread-safely, wrap with [RefCounted]
 */
interface Closeable {
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