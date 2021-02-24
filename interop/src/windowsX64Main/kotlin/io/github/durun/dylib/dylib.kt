package io.github.durun.dylib

import io.github.durun.io.Closeable
import io.github.durun.path.Path
import kotlinx.cinterop.*
import platform.posix.*
import platform.windows.FreeLibrary
import platform.windows.GetProcAddress
import platform.windows.HMODULE
import platform.windows.LoadLibrary

actual class Dylib(val handle: HMODULE) : Closeable {
	actual companion object {
		actual fun open(lib: String): Dylib {
			val handle = memScoped {
				LoadLibrary!!(lib.wcstr.ptr)
			}
			checkNotNull(handle) { "Load failed: $lib" }
			return Dylib(handle)
		}

		actual fun open(lib: Path): Dylib {
			check(access("$lib", F_OK) == 0) { "Not exists: $lib" }
			return if (lib.isAbsolute) open("$lib")
			else open(".\\$lib")
		}
	}

	actual override var isOpen: Boolean = true
		private set

	actual override fun close() {
		check(isOpen)
		FreeLibrary(handle)
		isOpen = false
	}

	inline fun <reified T : CPointed> getAddress(name: String): CPointer<T>? {
		val symbol = GetProcAddress(handle, name)?.reinterpret<T>()
		checkNotNull(symbol) { "Failed resolve $name" }
		return symbol
	}

	inline fun <reified T : CPointed> getSymbol(name: String): T {
		val address = getAddress<T>(name)
		checkNotNull(address)
		return address.pointed
	}

	inline fun <reified T : Function<*>> getFunction(name: String): CPointer<CFunction<T>> {
		val address = getAddress<CFunction<T>>(name)
		checkNotNull(address)
		return address
	}
}
