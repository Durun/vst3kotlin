package io.github.durun.vst3kotlin.base

import cwrapper.*
import io.github.durun.util.CClass
import kotlinx.cinterop.*

actual class BStream(thisPtr: CPointer<IBStream>) : FUnknown(thisPtr), CClass {
	override val ptr: CPointer<IBStream> get() = thisRawPtr.reinterpret()

	actual fun read(numBytes: Int): ByteArray {
		return memScoped {
			val buffer = allocArray<ByteVar>(numBytes)    // TODO: pool buffer
			val numBytesRead = alloc<IntVar>()
			val result = IBStream_read(this@BStream.ptr, buffer, numBytes, numBytesRead.ptr)
			check(result == kResultTrue) { result.kResultString }
			buffer.readBytes(numBytesRead.value)
		}
	}

	actual fun write(bytes: ByteArray, numBytes: Int): Int {
		return memScoped {
			val buffer = bytes.toCValues().ptr
			val numBytesWritten = alloc<IntVar>()
			val result = IBStream_write(this@BStream.ptr, buffer, numBytes, numBytesWritten.ptr)
			check(result == kResultTrue) { result.kResultString }
			numBytesWritten.value
		}
	}

	actual fun seek(pos: Long, mode: StreamSeekMode): Long {
		return memScoped {
			val newPos = alloc<LongVar>()
			val result = IBStream_seek(this@BStream.ptr, pos, mode.value, newPos.ptr)
			check(result == kResultTrue) { result.kResultString }
			newPos.value
		}
	}

	actual val pos: Long
		get() = memScoped {
			val pos = alloc<LongVar>()
			IBStream_tell(this@BStream.ptr, pos.ptr)
			pos.value
		}
}