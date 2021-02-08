package io.github.durun.dylib

import kotlinx.cinterop.*
import platform.posix.*

actual class DylibScope(
	val handle: CPointer<*>
) {
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

actual fun <T> useDylib(lib: String, body: DylibScope.() -> T?): T? {
	val handle = dlopen(lib, RTLD_LAZY)
	checkNotNull(handle) { "Load failed: $lib" }

	val result = runCatching {
		DylibScope(handle).body()
	}

	dlclose(handle)

	return result.getOrThrow()
}