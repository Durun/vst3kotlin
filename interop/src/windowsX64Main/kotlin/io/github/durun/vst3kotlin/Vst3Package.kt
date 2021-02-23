package io.github.durun.vst3kotlin

import cwrapper.IPluginFactory
import io.github.durun.dylib.Dylib
import io.github.durun.io.Closeable
import io.github.durun.path.Path
import io.github.durun.vst3kotlin.base.PluginFactory
import kotlinx.cinterop.CPointer
import kotlinx.cinterop.invoke

actual class Vst3Package
private constructor(
	private val lib: Dylib
) : Closeable {
	actual companion object {
		actual fun open(vst3: Path): Vst3Package {
			val soFile = vst3.resolve("Contents/x86_64-win/${vst3.nameWithoutExtension}.vst3")
			val lib = Dylib.open(soFile)
			return Vst3Package(lib)
		}
	}

	actual override var isOpen: Boolean = true
		private set

	actual override fun close() {
		check(isOpen)
		lib.close()
		isOpen = false
	}

	actual fun openPluginFactory(): PluginFactory {
		val proc = lib.getFunction<() -> CPointer<IPluginFactory>>("GetPluginFactory")
		return PluginFactory(proc.invoke())
	}
}