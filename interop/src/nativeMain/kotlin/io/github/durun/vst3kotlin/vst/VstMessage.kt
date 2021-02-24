package io.github.durun.vst3kotlin.vst

import cwrapper.IMessage
import cwrapper.IMessage_getAttributes
import cwrapper.IMessage_getMessageID
import cwrapper.IMessage_setMessageID
import io.github.durun.vst3kotlin.cppinterface.CClass
import io.github.durun.vst3kotlin.base.FUnknown
import kotlinx.cinterop.CPointer
import kotlinx.cinterop.CStructVar
import kotlinx.cinterop.reinterpret
import kotlinx.cinterop.toKString

class VstMessage(
	override val ptr: CPointer<IMessage>
) : FUnknown() {
	var messageID: String
		get() = (IMessage_getMessageID(this.ptr) ?: throw IllegalStateException()).toKString()
		set(value) = IMessage_setMessageID(this.ptr, value)
	val attributes: AttributeList
		get() = AttributeList(IMessage_getAttributes(this.ptr) ?: throw IllegalStateException())
}