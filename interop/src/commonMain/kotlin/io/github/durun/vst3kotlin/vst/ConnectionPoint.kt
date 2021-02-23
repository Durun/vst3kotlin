package io.github.durun.vst3kotlin.vst

import io.github.durun.util.CClass
import io.github.durun.vst3kotlin.base.FUnknown

expect class ConnectionPoint : FUnknown, CClass {
	fun connect(other: ConnectionPoint)
	fun disconnect(other: ConnectionPoint)
	fun notify(message: VstMessage)
}