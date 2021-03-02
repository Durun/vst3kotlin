package io.github.durun.dylib


import io.github.durun.io.Path
import io.github.durun.resource.Closeable
import kotlinx.cinterop.CFunction
import kotlinx.cinterop.CPointed
import kotlinx.cinterop.CPointer

expect class Dylib: Closeable {
    companion object {
        fun open(libName: String): Dylib
        fun open(libPath: Path): Dylib
    }

    val name:String
    override fun close()
    fun <T : CPointed> getAddress(name: String): CPointer<T>
    fun <T : Function<*>> getFunction(name: String): CPointer<CFunction<T>>
}


