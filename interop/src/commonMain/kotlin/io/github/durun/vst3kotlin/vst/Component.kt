package io.github.durun.vst3kotlin.vst

import io.github.durun.vst3kotlin.base.PluginBase
import io.github.durun.vst3kotlin.base.UID

expect class Component : PluginBase {
	val controllerClassID: UID
}