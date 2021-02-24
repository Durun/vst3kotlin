package io.github.durun.io

import kotlin.native.concurrent.AtomicInt

actual class RefCounted<T>
actual constructor(
    private val onOpen: () -> T,
    private val onClose: (T) -> Unit
) {
    private var refCount: AtomicInt = AtomicInt(0)
    private var _value: T? = null

    actual val value: T? get() = _value

    actual fun open(): T {
        val count = refCount.addAndGet(1)
        return value ?: run {   // if not open yet
            check(count == 1)
            val newValue = onOpen()
            _value = newValue
            newValue
        }
    }

    actual fun close() {
        val count = refCount.addAndGet(-1)
        check(0 <= count) { "already closed" }
        if (count == 0) {
            onClose(value ?: throw Exception("Internal error: reference is null"))
            _value = null
        }
    }

    actual fun <R> use(block: (T) -> R): R {
        val value = open()
        val result = runCatching { block(value) }
        close()
        return result.getOrThrow()
    }
}