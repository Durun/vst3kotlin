package io.github.durun.vst3kotlin.base

import cwrapper.*
import cwrapper.IPluginFactory_createInstance
import kotlinx.cinterop.*

fun cwrapper.TUID.toTUID(): TUID {
	return TUID(this.pointed.readValues(16).getBytes())
}

fun IPluginFactory_createInstance(
	this_ptr: CValuesRef<IPluginFactory>,
	classID: TUID,
	interfaceID: TUID,
	obj: CValuesRef<COpaquePointerVar>
): tresult = memScoped {
	val cid = CFuidOf(classID)
	val iid = CFuidOf(interfaceID)
	IPluginFactory_createInstance(this_ptr, cid.ptr, iid.ptr, obj)
}

fun MemScope.CTuidOf(id: TUID): cwrapper.TUID {
	val buf = allocArray<ByteVar>(id.bytes.size)
	id.bytes.forEachIndexed { i, byte ->
		buf[i] = byte
	}
	return buf
}

fun MemScope.CFuidOf(id: TUID): FUID {
	val fuid = alloc<FUID>()
	id.bytes.forEachIndexed { i, byte ->
		fuid.tuid[i] = byte
	}
	return fuid
}
