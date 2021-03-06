package io.github.durun.vst3kotlin.pluginterface.vst

import cwrapper.*
import io.github.durun.vst3kotlin.Adapter
import io.github.durun.vst3kotlin.pluginterface.base.FUnknown
import io.github.durun.vst3kotlin.pluginterface.base.kResultString
import kotlinx.cinterop.*

class AttributeList(
	override val ptr: CPointer<IAttributeList>
) : FUnknown() {
	val int: AttrProperty<Long> = object : AttrProperty<Long> {
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
	val float: AttrProperty<Double> = object : AttrProperty<Double> {
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

	@ExperimentalUnsignedTypes
	fun getString(id: AttrID, sizeInBytes: Int): String {
		return memScoped {
			val buf = allocArray<ShortVar>(sizeInBytes)
			val result = Adapter.IAttributeList.getString(ptr, id, buf, sizeInBytes.toUInt())
			check(result == kResultOk) { result.kResultString }
			buf.toKStringFromUtf16()
		}
	}

	fun setString(id: AttrID, value: String) {
		memScoped {
			val result = IAttributeList_setString(ptr, id, value.utf16.ptr.reinterpret())
			check(result == kResultOk) { result.kResultString }
		}
	}

	@ExperimentalUnsignedTypes
	fun getBinary(id: AttrID, size: Int): ByteArray {
		return memScoped {
			val buf = allocArray<ByteVar>(size)
			val readSize = alloc<UIntVar>()
			val result = IAttributeList_getBinary(ptr, id, buf.reinterpret(), readSize.ptr)
			check(result == kResultOk) { result.kResultString }
			buf.readBytes(readSize.value.toInt())
		}
	}

	@ExperimentalUnsignedTypes
	fun setBinary(id: AttrID, value: ByteArray) {
		memScoped {
			val result = IAttributeList_setBinary(ptr, id, value.toCValues().ptr, value.size.toUInt())
			check(result == kResultOk) { result.kResultString }
		}
	}
}