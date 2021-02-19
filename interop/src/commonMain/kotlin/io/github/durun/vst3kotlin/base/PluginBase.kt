package io.github.durun.vst3kotlin.base

expect abstract class PluginBase : FUnknown {
	fun initialize(context: HostContext?)
	fun terminate()
}