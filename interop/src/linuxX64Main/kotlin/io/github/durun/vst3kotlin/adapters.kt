package io.github.durun.vst3kotlin

import cwrapper.IAttributeList_getString
import cwrapper.IPlugView_onKeyDown
import cwrapper.IPlugView_onKeyUp
import cwrapper.tresult
import kotlinx.cinterop.CArrayPointer
import kotlinx.cinterop.CPointer
import kotlinx.cinterop.ShortVar

object Adapter {
	object IPlugView {
		fun onKeyDown(ptr: CPointer<cwrapper.IPlugView>, key: Short, keyCode: Short, modifiers: Short): tresult {
			return IPlugView_onKeyDown(ptr, key, keyCode, modifiers)
		}

		fun onKeyUp(ptr: CPointer<cwrapper.IPlugView>, key: Short, keyCode: Short, modifiers: Short): tresult {
			return IPlugView_onKeyUp(ptr, key, keyCode, modifiers)
		}
	}

	object IAttributeList {
		fun getString(
			ptr: CPointer<cwrapper.IAttributeList>,
			id: String?,
			buf: CArrayPointer<ShortVar>,
			sizeInBytes: UInt
		): tresult {
			return IAttributeList_getString(ptr, id, buf, sizeInBytes)
		}
	}
}