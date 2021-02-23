package io.github.durun.vst3kotlin.vst

import cwrapper.*
import io.github.durun.util.CClass
import io.github.durun.vst3kotlin.Adapter
import io.github.durun.vst3kotlin.base.FUnknown
import io.github.durun.vst3kotlin.base.kResultString
import kotlinx.cinterop.*

actual class AttributeList(
	ptr: CPointer<IAttributeList>
) : FUnknown(ptr), CClass {
	override val ptr: CPointer<IAttributeList> get() = thisRawPtr.reinterpret()

	actual val int: AttrProperty<Long> = object : AttrProperty<Long> {
		override fun get(id: AttrID): Long {
			return memScoped {
				val buf: LongVar = alloc()
				val result = IAttributeList_getInt(ptr, id, buf.ptr)
				check(result == kResultOk) { result.kResultString }
				buf.value
			}
		}

		override fun set(id: AttrID, value: Long) {
			val result = IAttributeList_setInt(ptr, id, value)
			check(result == kResultOk) { result.kResultString }
		}
	}
	actual val float: AttrProperty<Double> = object : AttrProperty<Double> {
		override fun get(id: AttrID): Double {
			return memScoped {
				val buf: DoubleVar = alloc()
				val result = IAttributeList_getFloat(ptr, id, buf.ptr)
				check(result == kResultOk) { result.kResultString }
				buf.value
			}
		}

		override fun set(id: AttrID, value: Double) {
			val result = IAttributeList_setFloat(ptr, id, value)
			check(result == kResultOk) { result.kResultString }
		}
	}

	actual fun getString(id: AttrID, sizeInBytes: Int): String {
		return memScoped {
			val buf = allocArray<ShortVar>(sizeInBytes)
			val result = Adapter.IAttributeList.getString(ptr, id, buf, sizeInBytes.toUInt())
			check(result == kResultOk) { result.kResultString }
			buf.toKStringFromUtf16()
		}
	}

	actual fun setString(id: AttrID, value: String) {
		memScoped {
			val result = IAttributeList_setString(ptr, id, value.utf16.ptr.reinterpret())
			check(result == kResultOk) { result.kResultString }
		}
	}

	actual fun getBinary(id: AttrID, size: Int): ByteArray {
		return memScoped {
			val buf = allocArray<ShortVar>(size)
			val result = IAttributeList_getString(ptr, id, buf, size.toUInt())
			check(result == kResultOk) { result.kResultString }
			buf.readBytes(size)
		}
	}

	actual fun setBinary(id: AttrID, value: ByteArray) {
		memScoped {
			val result = IAttributeList_setBinary(ptr, id, value.toCValues().ptr, value.size.toUInt())
			check(result == kResultOk) { result.kResultString }
		}
	}
}