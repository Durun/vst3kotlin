package io.github.durun.vst3kotlin.hosting

import cwrapper.IPluginFactory
import io.github.durun.dylib.Dylib
import io.github.durun.io.Closeable
import io.github.durun.log.logger
import io.github.durun.path.Path
import io.github.durun.vst3kotlin.base.PluginFactory
import kotlinx.cinterop.CFunction
import kotlinx.cinterop.CPointer
import kotlinx.cinterop.invoke

expect object ModuleUtil {
    fun moduleNameOf(vst3Path: Path): String
    fun libPathOf(vst3Path: Path): Path
    fun entryFuncOf(lib: Dylib): CPointer<CFunction<() -> Unit>>
    fun exitFuncOf(lib: Dylib): CPointer<CFunction<() -> Unit>>
    fun factoryGetterOf(lib: Dylib): CPointer<CFunction<() -> CPointer<IPluginFactory>>>
}

class Module
private constructor(
    val libPath: Path,
    private val lib: Dylib
) : Closeable {
    private val log by logger()
    companion object {
        private val log by logger()
        fun of(path: Path): Module {
            val (libPath, lib) = runCatching {
                val libPath = ModuleUtil.libPathOf(path)
                libPath to Dylib.open(libPath)
            }.recoverCatching {
                path to Dylib.open(path)
            }.getOrThrow()
            log.info { "Open Module $libPath" }
            return Module(libPath, lib)
        }
    }

    val name: String = lib.name
    val factory: PluginFactory

    override var isOpen: Boolean = true
        private set

    init {
        // Initialize module
        runCatching {
            ModuleUtil.entryFuncOf(lib).invoke()
        }.onFailure {
            throw Exception("Failed to call entry function", it)
        }
        Companion.log.info { "Succes Entry $libPath" }
        // Get Plugin factory
        val factoryPtr = runCatching {
            ModuleUtil.factoryGetterOf(lib).invoke()
        }.onFailure {
            throw Exception("Failed to call 'GetPluginFactory'", it)
        }.getOrThrow()
        Companion.log.info { "Succes GetPluginFactory $libPath" }
        factory = PluginFactory(factoryPtr)
    }

    override fun close() {
        isOpen = false
        ModuleUtil.exitFuncOf(lib)
        lib.close()
        log.info { "Closed Module $libPath" }
    }
}