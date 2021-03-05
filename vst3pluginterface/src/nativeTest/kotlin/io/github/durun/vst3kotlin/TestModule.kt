package io.github.durun.vst3kotlin

import io.github.durun.dylib.Dylib
import io.github.durun.io.Path
import io.github.durun.resource.Closeable
import io.github.durun.util.logger
import io.github.durun.vst3kotlin.pluginterface.base.PluginFactory
import kotlinx.cinterop.invoke

class TestModule
private constructor(
    private val lib: Dylib
) : Closeable {
    private val log by logger()

    companion object {
        private val log by logger()
        fun open(path: Path): TestModule {
            val (libPath, lib) = runCatching {
                val libPath = ModuleUtil.libPathOf(path)
                libPath to Dylib.open(libPath)
            }.recoverCatching {
                log.warn { "Not found ${ModuleUtil.libPathOf(path)}. Open instead $path" }
                path to Dylib.open(path)
            }.getOrThrow()
            log.info { "Open Module $libPath" }
            return TestModule(lib)
        }
    }

    val name: String = lib.name
    val factory: PluginFactory

    override var isOpen: Boolean = true
        private set

    init {
        log.info { "Initializing $lib" }
        // Initialize module
        val entryFunc = runCatching { ModuleUtil.entryFuncOf(lib) }
            .onSuccess { log.info { "Found the entry function" } }
            .onFailure { log.warn { "Entry function was not found. Ignored it." } }
            .getOrNull()
        runCatching { entryFunc?.invoke() }
            .onSuccess { if (it != null) log.info { "Called entry function" } }
            .onFailure { log.error { "Failed to call entry function" } }
            .getOrThrow()
        // Get Plugin factory
        val factoryPtr = runCatching { ModuleUtil.factoryGetterOf(lib) }
            .onSuccess { log.info { "Found GetPluginFactory" } }
            .onFailure { log.error { "GetPluginFactory was not found" } }
            .mapCatching { it.invoke() }
            .onSuccess { log.info { "Called GetPluginFactory" } }
            .onFailure { throw Exception("Failed to call 'GetPluginFactory'", it) }
            .getOrThrow()
        factory = PluginFactory(factoryPtr)
        log.info { "Got PluginFactory: $lib" }
    }

    override fun close() {
        log.info { "Closing $lib" }
        isOpen = false
        factory.close()
        log.info { "Closed factory" }
        val exitFunc = runCatching { ModuleUtil.exitFuncOf(lib) }
            .onSuccess { log.info { "Found the exit function" } }
            .onFailure { log.warn { "Exit function was not found. Ignored it." } }
            .getOrNull()
        runCatching { exitFunc?.invoke() }
            .onSuccess { if (it != null) log.info { "Called exit function" } }
            .onFailure { log.error { "Failed to call exit function" } }
        lib.close()
        log.info { "Closed Module: $lib" }
    }
}