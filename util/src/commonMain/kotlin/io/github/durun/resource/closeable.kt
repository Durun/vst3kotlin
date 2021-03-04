package io.github.durun.resource

/**
 * [Closeable] is not thread-safe.
 * If you want to manage thread-safely, wrap with [Shared]
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