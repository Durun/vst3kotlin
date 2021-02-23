package io.github.durun.vst3kotlin

import cwrapper.IAttributeList_getString
import cwrapper.IPlugView_onKeyDown
import cwrapper.IPlugView_onKeyUp
import cwrapper.tresult
import kotlinx.cinterop.CArrayPointer
import kotlinx.cinterop.CPointer
import kotlinx.cinterop.ShortVar
import kotlinx.cinterop.reinterpret

object Adapter {
	object IPlugView {
		@ExperimentalUnsignedTypes
		fun onKeyDown(ptr: CPointer<cwrapper.IPlugView>, key: Short, keyCode: Short, modifiers: Short): tresult {
			return IPlugView_onKeyDown(ptr, key.toUShort(), keyCode, modifiers)
		}

		@ExperimentalUnsignedTypes
		fun onKeyUp(ptr: CPointer<cwrapper.IPlugView>, key: Short, keyCode: Short, modifiers: Short): tresult {
			return IPlugView_onKeyUp(ptr, key.toUShort(), keyCode, modifiers)
		}
	}
	object IAttributeList{
		@ExperimentalUnsignedTypes
		fun getString(
			ptr: CPointer<cwrapper.IAttributeList>,
			id: String?,
			buf: CArrayPointer<ShortVar>,
			sizeInBytes: UInt
		): tresult {
			return IAttributeList_getString(ptr, id, buf.reinterpret(), sizeInBytes)
		}
	}
}