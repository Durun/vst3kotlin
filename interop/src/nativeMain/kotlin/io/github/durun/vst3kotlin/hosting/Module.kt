package io.github.durun.vst3kotlin.hosting

import io.github.durun.path.Path
import io.github.durun.vst3kotlin.base.PluginFactory

expect class Module {
    companion object {
        fun of(modulePath: Path): Module
    }
    val name:String
    val path:Path
    val factory:PluginFactory
}