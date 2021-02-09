package io.github.durun.vst3kotlin

import cwrapper.*
import io.github.durun.dylib.Dylib
import io.github.durun.io.Closeable
import io.github.durun.path.Path
import kotlinx.cinterop.*
import kotlin.native.concurrent.AtomicInt

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

	actual override var isOpen: Boolean = true
	private set

	actual override fun close() {
		check(isOpen)
		lib.close()
		isOpen = false
	}

	actual fun openPluginFactory(): PluginFactory {
		val proc = lib.getFunction<() -> CPointer<IPluginFactory>>("GetPluginFactory")
		return PluginFactory(proc.invoke())
	}
}

actual class PluginFactory(
	private val factoryPtr: CPointer<IPluginFactory>
): FUnknown {
	actual override var isOpen: Boolean = true
		private set
	actual val factoryInfo: FactoryInfo by lazy {
		memScoped {
			val infoPtr = alloc<PFactoryInfo>().ptr
			IPluginFactory_getFactoryInfo(factoryPtr, infoPtr)
			infoPtr.pointed.toKFactoryInfo()
		}
	}

	actual val classInfo: List<ClassInfo> by lazy {
		val nClass = IPluginFactory_countClasses(factoryPtr)
		memScoped {
			val infoPtr = alloc<PClassInfo>().ptr
			(0 until nClass).map { i ->
				IPluginFactory_getClassInfo(factoryPtr, i, infoPtr)
				infoPtr.pointed.toKClassInfo()
			}
		}.toList()
	}

	actual override fun close() {
		check(isOpen)
		IPluginFactory_release(factoryPtr)
		isOpen = false
	}

	@OptIn(ExperimentalUnsignedTypes::class)
	private fun PFactoryInfo.toKFactoryInfo(): FactoryInfo {
		return FactoryInfo(
			vendor = vendor.toKString(),
			url = url.toKString(),
			email = email.toKString(),
			flags = FactoryInfo.Flags(
				NoFlags = flags == kNoFlags,
				ClassesDiscardable = (flags and kClassesDiscardable) != 0u,
				LicenseCheck = (flags and kLicenseCheck) != 0u,
				ComponentNonDiscardable = (flags and kComponentNonDiscardable) != 0u,
				Unicode = (flags and kUnicode) != 0u
			)
		)
	}

	private fun PClassInfo.toKClassInfo(): ClassInfo {
		return ClassInfo(
			classId = TUID(cid.readBytes(16)),
			cardinality = cardinality,
			category = category.toKString(),
			name = name.toKString()
		)
	}
}