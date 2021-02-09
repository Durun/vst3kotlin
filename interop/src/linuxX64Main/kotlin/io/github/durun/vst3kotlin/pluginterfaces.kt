package io.github.durun.vst3kotlin

import cwrapper.*
import io.github.durun.dylib.Dylib
import io.github.durun.io.Closeable
import io.github.durun.path.Path
import kotlinx.cinterop.*

actual class Vst3Package
private constructor(
	private val lib: Dylib
) : Closeable {
	actual companion object {
		actual fun open(vst3: Path): Vst3Package {
			val soFile = vst3.resolve("Contents/x86_64-linux/${vst3.nameWithoutExtension}.so")
			val lib = Dylib.open(soFile)
			return Vst3Package(lib)
		}
	}

	actual override fun close() {
		lib.close()
	}

	actual val pluginFactory: PluginFactory by lazy {
		val proc = lib.getFunction<() -> CPointer<IPluginFactory>>("GetPluginFactory")
		PluginFactory(proc.invoke())
	}
}

actual class PluginFactory(
	private val factoryPtr: CPointer<IPluginFactory>
) {
	@OptIn(ExperimentalUnsignedTypes::class)
	actual val factoryInfo: FactoryInfo by lazy {
		memScoped {
			val infoPtr = alloc<PFactoryInfo>().ptr
			IPluginFactory_getFactoryInfo(factoryPtr, infoPtr)
			val info = infoPtr.pointed
			FactoryInfo(
				vendor = info.vendor.toKString(),
				url = info.url.toKString(),
				email = info.email.toKString(),
				flags = FactoryInfo.Flags(
					NoFlags = info.flags == kNoFlags,
					ClassesDiscardable = (info.flags and kClassesDiscardable) != 0u,
					LicenseCheck = (info.flags and kLicenseCheck) != 0u,
					ComponentNonDiscardable = (info.flags and kComponentNonDiscardable) != 0u,
					Unicode = (info.flags and kUnicode) != 0u
				)
			)
		}
	}
}