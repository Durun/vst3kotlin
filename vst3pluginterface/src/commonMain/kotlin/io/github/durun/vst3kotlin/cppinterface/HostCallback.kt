package io.github.durun.vst3kotlin.cppinterface

import io.github.durun.resource.Closeable

expect object HostCallback : CClass, Closeable {
	fun receiveMessages(): Sequence<Message>
}