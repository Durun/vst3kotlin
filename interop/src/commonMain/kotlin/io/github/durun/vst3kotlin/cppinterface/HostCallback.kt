package io.github.durun.vst3kotlin.cppinterface

import io.github.durun.io.Closeable

expect object HostCallback : Closeable {
	fun receiveMessages(): Sequence<Message>
}