package io.github.durun.vst3kotlin.base

import io.github.durun.util.CClass

expect class BStream : FUnknown, CClass {
	fun read(numBytes: Int): ByteArray
	fun write(bytes: ByteArray, numBytes: Int): Int // returns amount of written bytes
	fun seek(pos: Long, mode: StreamSeekMode): Long // returns new sek position
	val pos: Long // current stream read-write position
}

enum class StreamSeekMode(val value: Int) {
	Set(0), Current(1), End(2)
}