package io.github.durun.vst3kotlin.hosting

import cwrapper.IPluginFactory
import io.github.durun.dylib.Dylib
import io.github.durun.io.Path
import kotlinx.cinterop.CFunction
import kotlinx.cinterop.CPointer

internal expect object ModuleUtil {
	fun moduleNameOf(vst3Path: Path): String
	fun libPathOf(vst3Path: Path): Path
	fun entryFuncOf(lib: Dylib): CPointer<CFunction<() -> Unit>>
	fun exitFuncOf(lib: Dylib): CPointer<CFunction<() -> Unit>>
	fun factoryGetterOf(lib: Dylib): CPointer<CFunction<() -> CPointer<IPluginFactory>>>
}