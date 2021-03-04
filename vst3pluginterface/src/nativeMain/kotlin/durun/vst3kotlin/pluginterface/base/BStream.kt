package io.github.durun.vst3kotlin.pluginterface.base

import cwrapper.*
import kotlinx.cinterop.*

enum class StreamSeekMode(val value: Int) {
	Set(0), Current(1), End(2)
}

class BStream(override val ptr: CPointer<IBStream>) : FUnknown() {

	fun read(numBytes: int32): ByteArray {
		return memScoped {
			val buffer = allocArray<ByteVar>(numBytes)    // TODO: pool buffer
			val numBytesRead = alloc<IntVar>()
			val result = IBStream_read(this@BStream.ptr, buffer, numBytes, numBytesRead.ptr)
			check(result == kResultTrue) { result.kResultString }
			buffer.readBytes(numBytesRead.value)
		}
	}

	fun write(bytes: ByteArray, numBytes: int32): Int {
		return memScoped {
			val buffer = bytes.toCValues().ptr
			val numBytesWritten = alloc<IntVar>()
			val result = IBStream_write(this@BStream.ptr, buffer, numBytes, numBytesWritten.ptr)
			check(result == kResultTrue) { result.kResultString }
			numBytesWritten.value
		}
	}

	fun seek(pos: int64, mode: StreamSeekMode): int64 {
		return memScoped {
			val newPos = alloc<LongVar>()
			val result = IBStream_seek(this@BStream.ptr, pos, mode.value, newPos.ptr)
			check(result == kResultTrue) { result.kResultString }
			newPos.value
		}
	}

	val pos: int64
		get() = memScoped {
			val pos = alloc<LongVar>()
			IBStream_tell(this@BStream.ptr, pos.ptr)
			pos.value
		}
}