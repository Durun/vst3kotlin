package io.github.durun.vst3kotlin.hosting

import cwrapper.IPluginFactory
import io.github.durun.dylib.Dylib
import io.github.durun.path.Path
import io.github.durun.path.exists
import kotlinx.cinterop.CFunction
import kotlinx.cinterop.CPointer

actual object ModuleUtil {
	actual fun moduleNameOf(vst3Path: Path): String {
		return vst3Path.nameWithoutExtension
	}

	actual fun libPathOf(vst3Path: Path): Path {
		val name = vst3Path.nameWithoutExtension
		return vst3Path.resolve("Contents/MacOS/$name").takeIf { it.exists() }
			?: vst3Path.takeIf { it.exists() }
			?: throw NoSuchElementException("Not found file: $vst3Path")
	}

	private const val entryName: String = "bundleEntry"
	private const val exitName: String = "bundleExit"
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