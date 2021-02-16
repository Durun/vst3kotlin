package io.github.durun.vst3kotlin.base

expect class BStream : FUnknown {
	fun read(numBytes: Int): ByteArray
	fun write(bytes: ByteArray, numBytes: Int): Int // returns amount of written bytes
	fun seek(pos: Long, mode: StreamSeekMode): Long // returns new sek position
	val pos: Long // current stream read-write position
}

enum class StreamSeekMode(val value: Int) {
	Set(0), Current(1), End(2)
}