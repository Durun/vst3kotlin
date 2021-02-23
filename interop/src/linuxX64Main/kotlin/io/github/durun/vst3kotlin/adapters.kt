package io.github.durun.vst3kotlin

import cwrapper.IPlugView_onKeyDown
import cwrapper.IPlugView_onKeyUp
import cwrapper.tresult
import kotlinx.cinterop.CPointer

object Adapter {
	object IPlugView {
		fun onKeyDown(ptr: CPointer<cwrapper.IPlugView>, key: Short, keyCode: Short, modifiers: Short): tresult {
			return IPlugView_onKeyDown(ptr, key, keyCode, modifiers)
		}

		fun onKeyUp(ptr: CPointer<cwrapper.IPlugView>, key: Short, keyCode: Short, modifiers: Short): tresult {
			return IPlugView_onKeyUp(ptr, key, keyCode, modifiers)
		}
	}
}