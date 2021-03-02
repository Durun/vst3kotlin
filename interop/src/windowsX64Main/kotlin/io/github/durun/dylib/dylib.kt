package io.github.durun.dylib

import io.github.durun.resource.Closeable
import io.github.durun.io.Path
import io.github.durun.io.exists
import kotlinx.cinterop.*
import platform.windows.FreeLibrary
import platform.windows.GetProcAddress
import platform.windows.HMODULE
import platform.windows.LoadLibrary

actual class Dylib
private constructor(
    actual val name: String,
    private val handle: HMODULE
) : Closeable {
    actual companion object {
        actual fun open(libName: String): Dylib {
            val handle = memScoped {
                LoadLibrary!!(libName.wcstr.ptr)
            } ?: throw IllegalArgumentException("Load failed: $libName")
            val name = Path.of(libName).nameWithoutExtension
            return Dylib(name, handle)
        }

        actual fun open(libPath: Path): Dylib {
            check(libPath.exists()) { "Not exist: $libPath" }
            val openPath = if (libPath.isAbsolute) libPath else Path.of(".").resolve(libPath)
            return open(openPath.toString())
        }
    }

    override var isOpen: Boolean = true
        private set
    actual override fun close() {
        check(isOpen)
        FreeLibrary(handle)
        isOpen = false
    }

    actual fun <T : CPointed> getAddress(name: String): CPointer<T> {
        val symbol = GetProcAddress(handle, name)?.reinterpret<T>()
        checkNotNull(symbol) { "Failed resolve $name" }
        return symbol
    }

    actual fun <T : Function<*>> getFunction(name: String): CPointer<CFunction<T>> {
        return getAddress(name)
    }
}
