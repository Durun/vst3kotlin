package io.github.durun.vst3kotlin.base

import cwrapper.PFactoryInfo
import cwrapper.SIPluginFactory
import io.github.durun.io.Closeable
import kotlinx.cinterop.*

class SPluginFactory(
	val ptr: CPointer<SIPluginFactory>
) : Closeable {
	override var isOpen: Boolean = true
		private set

	override fun close() {
		ptr.release()
		isOpen = false
	}

	fun getFactoryInfo(): FactoryInfo {
		return memScoped {
			val factory = alloc<PFactoryInfo>()
			ptr.getFactoryInfo(factory.ptr)
			factory.run {
				FactoryInfo(vendor.toKString(), url.toKString(), email.toKString(), FactoryInfo.Flags(flags.toInt()))
			}
		}
	}
}

fun CPointer<SIPluginFactory>.release() {
	val func = this.pointed.vtable?.pointed?.FUnknown?.release
	checkNotNull(func)
	func(this)
}

fun CPointer<SIPluginFactory>.getFactoryInfo(factory: CPointer<PFactoryInfo>?) {
	val func = this.pointed.vtable?.pointed?.getFactoryInfo
	checkNotNull(func)
	func(this, factory)
}