package io.github.durun.dylib

import io.github.durun.io.Closeable
import kotlinx.cinterop.*
import platform.posix.*

actual class Dylib (val handle: CPointer<*>): Closeable {
	actual companion object {
		actual fun open(lib: String): Dylib {
			val handle = dlopen(lib, RTLD_LAZY)
			checkNotNull(handle) { "Load failed: $lib" }
			return Dylib(handle)
		}
	}

	actual override fun close() {
		dlclose(handle)
	}

	inline fun <reified T : CPointed> getAddress(name: String): CPointer<T>? {
		dlerror()
		val symbol = dlsym(handle, name)?.reinterpret<T>()
		val error = dlerror()?.toKString()
		check(error == null) { "Failed resolve $name: $error" }
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
