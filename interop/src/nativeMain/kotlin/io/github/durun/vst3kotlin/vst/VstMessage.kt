package io.github.durun.vst3kotlin.vst

import cwrapper.IMessage
import cwrapper.IMessage_getAttributes
import cwrapper.IMessage_getMessageID
import cwrapper.IMessage_setMessageID
import io.github.durun.util.CClass
import io.github.durun.vst3kotlin.base.FUnknown
import kotlinx.cinterop.CPointer
import kotlinx.cinterop.reinterpret
import kotlinx.cinterop.toKString

actual class VstMessage(
	thisPtr: CPointer<IMessage>
) : FUnknown(thisPtr), CClass {
	override val ptr: CPointer<IMessage> get() = thisRawPtr.reinterpret()

	actual var messageID: String
		get() = (IMessage_getMessageID(this.ptr) ?: throw IllegalStateException()).toKString()
		set(value) = IMessage_setMessageID(this.ptr, value)
	actual val attributes: AttributeList
		get() = AttributeList(IMessage_getAttributes(this.ptr) ?: throw IllegalStateException())
}