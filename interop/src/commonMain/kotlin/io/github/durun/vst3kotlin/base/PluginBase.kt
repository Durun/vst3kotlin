package io.github.durun.vst3kotlin.base

import io.github.durun.util.CClass

expect abstract class PluginBase : FUnknown {
	fun initialize(context: CClass)
	fun terminate()
}