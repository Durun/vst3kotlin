package io.github.durun.vst3kotlin.cppinterface

import io.github.durun.io.Closeable
import io.github.durun.util.CClass

expect object HostCallback : CClass, Closeable {
	fun receiveMessages(): Sequence<Message>
}