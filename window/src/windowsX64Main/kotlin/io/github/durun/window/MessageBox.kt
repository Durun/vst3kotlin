package io.github.durun.window

import kotlinx.cinterop.invoke
import kotlinx.cinterop.memScoped
import kotlinx.cinterop.wcstr
import platform.windows.MB_ICONINFORMATION
import platform.windows.MessageBox

object MessageBox {
	@ExperimentalUnsignedTypes
	fun show(text: String, title: String = text) {
		memScoped {
			MessageBox?.invoke(null, text.wcstr.ptr, title.wcstr.ptr, MB_ICONINFORMATION.toUInt())
		}
	}
}