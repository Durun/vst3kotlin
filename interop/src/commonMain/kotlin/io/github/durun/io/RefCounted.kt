package io.github.durun.io

expect class RefCounted<T>(
    onOpen: () -> T,
    onClose: (T) -> Unit
) {
    val value: T?
    fun open(): T
    fun close()
    fun <R> use(block: (T) -> R): R
}