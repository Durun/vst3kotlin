package io.github.durun.resource

expect class Shared<T>(
    onOpen: () -> T,
    onClose: (T) -> Unit
) : Closeable {
    val value: T?
    fun open(): T
    fun <R> use(block: (T) -> R): R
}