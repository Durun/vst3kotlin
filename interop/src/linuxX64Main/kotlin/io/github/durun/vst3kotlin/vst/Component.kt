package io.github.durun.vst3kotlin.vst

import cwrapper.IComponent
import cwrapper.IComponent_getControllerClassId
import io.github.durun.vst3kotlin.base.PluginBase
import io.github.durun.vst3kotlin.base.UID
import io.github.durun.vst3kotlin.base.toUID
import kotlinx.cinterop.*

actual class Component(
	thisPtr: CPointer<IComponent>
) : PluginBase(thisPtr) {
	private val thisPtr get() = thisRawPtr.reinterpret<IComponent>()
	actual val controllerClassID: UID by lazy {
		memScoped {
			val tuid = allocArray<ByteVar>(16)
			val result = IComponent_getControllerClassId(thisPtr, tuid)
			tuid.toUID()
		}
	}
}