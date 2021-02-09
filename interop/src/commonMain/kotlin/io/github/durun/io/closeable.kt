package io.github.durun.io

interface Closeable {
	/**
	 * Not thread-safe
	 */
	val isOpen: Boolean
	fun close()
}