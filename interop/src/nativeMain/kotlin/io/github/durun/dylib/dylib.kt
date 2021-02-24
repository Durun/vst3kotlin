package io.github.durun.dylib


import io.github.durun.io.Closeable
import io.github.durun.path.Path
import kotlinx.cinterop.*

expect class Dylib: Closeable {
    companion object {
        fun open(libName: String): Dylib
        fun open(libPath: Path): Dylib
    }

    override fun close()
    fun <T : CPointed> getAddress(name: String): CPointer<T>
    fun <T : Function<*>> getFunction(name: String): CPointer<CFunction<T>>
}


