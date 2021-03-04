package io.github.durun.vst3kotlin

import cwrapper.IPluginFactory
import io.github.durun.dylib.Dylib
import io.github.durun.io.Path
import io.github.durun.io.exists
import io.github.durun.resource.Closeable
import io.github.durun.util.logger
import io.github.durun.vst3kotlin.pluginterface.base.PluginFactory
import kotlinx.cinterop.CFunction
import kotlinx.cinterop.CPointer
import kotlinx.cinterop.invoke

actual object ModuleUtil {
	actual fun moduleNameOf(vst3Path: Path): String {
		return vst3Path.nameWithoutExtension
	}

	actual fun libPathOf(vst3Path: Path): Path {
		val name = vst3Path.nameWithoutExtension
		return vst3Path.resolve("Contents/x86_64-linux/$name.so").takeIf { it.exists() }
			?: vst3Path.takeIf { it.exists() }
			?: throw NoSuchElementException("Not found file: $vst3Path")
	}

	private const val entryName: String = "ModuleEntry"
	private const val exitName: String = "ModuleExit"
	private const val factoryGetterName: String = "GetPluginFactory"

	actual fun entryFuncOf(lib: Dylib): CPointer<CFunction<() -> Unit>> {
		return lib.getFunction(entryName)
	}

	actual fun exitFuncOf(lib: Dylib): CPointer<CFunction<() -> Unit>> {
		return lib.getFunction(exitName)
	}

	actual fun factoryGetterOf(lib: Dylib): CPointer<CFunction<() -> CPointer<IPluginFactory>>> {
		return lib.getFunction(factoryGetterName)
	}
}


