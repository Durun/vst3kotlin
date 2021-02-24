package io.github.durun.vst3kotlin.hosting

import io.github.durun.path.Path
import io.github.durun.vst3kotlin.base.PluginFactory

actual class Module {
    actual companion object {
        actual fun of(modulePath: Path): Module = TODO()
    }

    actual val path: Path = TODO()
    actual val name: String = TODO()
    actual val factory: PluginFactory = TODO()
}