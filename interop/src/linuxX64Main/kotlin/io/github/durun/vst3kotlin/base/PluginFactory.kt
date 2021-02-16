package io.github.durun.vst3kotlin.base

import cwrapper.*
import kotlinx.cinterop.*

actual class PluginFactory(
	private val factoryPtr: CPointer<IPluginFactory>
) : FUnknown {
	private val factory2Ptr: CPointer<IPluginFactory2>? = memScoped {
		val ptrPtr = alloc<CPointerVarOf<CPointer<IPluginFactory2>>>().ptr
		val result = IPluginFactory_queryInterface(factoryPtr, IPluginFactory2_iid, ptrPtr.reinterpret())
		check(result == kResultOk)
		ptrPtr.pointed.value
	}
	private val factory3Ptr: CPointer<IPluginFactory3>? = memScoped {
		val ptrPtr = alloc<CPointerVarOf<CPointer<IPluginFactory3>>>().ptr
		val result = IPluginFactory_queryInterface(factoryPtr, IPluginFactory3_iid, ptrPtr.reinterpret())
		check(result == kResultOk)
		ptrPtr.pointed.value
	}

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
		factory2Ptr?.let { IPluginFactory_release(it.reinterpret()) }
		factory3Ptr?.let { IPluginFactory_release(it.reinterpret()) }
		isOpen = false
	}

	private fun IPluginFactory_createInstance(
		this_ptr: CValuesRef<IPluginFactory>,
		classID: UID,
		interfaceID: UID,
		obj: CValuesRef<COpaquePointerVar>
	): tresult = memScoped {
		val cid = classID.toFuidPtr(this)
		val iid = interfaceID.toFuidPtr(this)
		IPluginFactory_createInstance(this_ptr, cid, iid, obj)
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

	@OptIn(ExperimentalUnsignedTypes::class)
	private fun PClassInfo.toKClassInfo(info2: PClassInfo2? = null, info3: PClassInfoW? = null): ClassInfo {
		return ClassInfo(
			classId = UID((info3?.cid ?: info2?.cid ?: cid).readBytes(16)),
			cardinality = info3?.cardinality ?: info2?.cardinality ?: cardinality,
			category = (info3?.category ?: info2?.category ?: category).toKString(),
			name = info3?.name?.toKStringFromUtf16() ?: (info2?.name ?: name).toKString(),
			subCategories = (info3?.subCategories ?: info2?.subCategories)?.toKString(),
			vendor = info3?.vendor?.toKStringFromUtf16() ?: info2?.vendor?.toKString(),
			version = info3?.version?.toKStringFromUtf16() ?: info2?.version?.toKString(),
			sdkVersion = info3?.sdkVersion?.toKStringFromUtf16() ?: info2?.sdkVersion?.toKString(),
			flags = (info3?.classFlags ?: info2?.classFlags)?.let { ClassInfo.Flags(it.toInt()) }
		)
	}
}