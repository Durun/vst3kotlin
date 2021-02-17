package io.github.durun.vst3kotlin.vst

import cwrapper.IProcessContextRequirements
import cwrapper.IProcessContextRequirements_getProcessContextRequirements
import io.github.durun.vst3kotlin.base.FUnknown
import kotlinx.cinterop.CPointer
import kotlinx.cinterop.reinterpret

actual class ProcessContextRequirements(
	thisPtr: CPointer<IProcessContextRequirements>
) : FUnknown(thisPtr) {
	private val thisPtr: CPointer<IProcessContextRequirements> get() = thisRawPtr.reinterpret()
	actual val processContextRequirements: ProcessContextRequirement
		get() {
			val flags = IProcessContextRequirements_getProcessContextRequirements(thisPtr)
			return ProcessContextRequirement(flags)
		}
}