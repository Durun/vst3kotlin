package io.github.durun.dylib

import io.github.durun.io.Closeable
import io.github.durun.path.Path
import io.github.durun.path.exists
import kotlinx.cinterop.*
import platform.posix.*

actual class Dylib
private constructor(
	actual val name: String,
	private val handle: CPointer<*>
) : Closeable {
	actual companion object {
		actual fun open(libName: String): Dylib {
			val handle = dlopen(libName, RTLD_LAZY)
			checkNotNull(handle) { "Load failed: $libName" }
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
		dlclose(handle)
		isOpen = false
	}

	actual fun <T : CPointed> getAddress(name: String): CPointer<T> {
		dlerror()
		val symbol = dlsym(handle, name)?.reinterpret<T>()
		val error = dlerror()?.toKString()
		checkNotNull(symbol) { "Failed resolve $name: $error" }
		return symbol
	}

	actual fun <T : Function<*>> getFunction(name: String): CPointer<CFunction<T>> {
		return getAddress(name)
	}
}
