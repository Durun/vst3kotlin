package io.github.durun.vst3kotlin.base

import cwrapper.*
import io.github.durun.util.logger
import io.github.durun.vst3kotlin.InterfaceID
import io.github.durun.vst3kotlin.pluginterface.base.ClassInfo
import io.github.durun.vst3kotlin.pluginterface.base.FactoryInfo
import io.github.durun.vst3kotlin.pluginterface.base.UID
import io.github.durun.vst3kotlin.pluginterface.base.VstClassCategory
import io.github.durun.vst3kotlin.vst.AudioProcessor
import io.github.durun.vst3kotlin.vst.Component
import io.github.durun.vst3kotlin.vst.EditController
import kotlinx.cinterop.*

class PluginFactory(
    override val ptr: CPointer<IPluginFactory>
) : FUnknown() {
	private val log by logger()
	private val thisPtr get() = ptr.reinterpret<IPluginFactory>()
	private val factory2Ptr: CPointer<IPluginFactory2>? = memScoped {
		val ptrPtr = alloc<CPointerVarOf<CPointer<IPluginFactory2>>>().ptr
		val result = IPluginFactory_queryInterface(thisPtr, IPluginFactory2_iid, ptrPtr.reinterpret())
		check(result == kResultOk)
		ptrPtr.pointed.value
	}
	private val factory3Ptr: CPointer<IPluginFactory3>? = memScoped {
		val ptrPtr = alloc<CPointerVarOf<CPointer<IPluginFactory3>>>().ptr
		val result = IPluginFactory_queryInterface(thisPtr, IPluginFactory3_iid, ptrPtr.reinterpret())
		check(result == kResultOk)
		ptrPtr.pointed.value
	}

    @ExperimentalUnsignedTypes
    val factoryInfo: FactoryInfo by lazy {
        memScoped {
            val infoPtr = alloc<PFactoryInfo>().ptr
            IPluginFactory_getFactoryInfo(thisPtr, infoPtr)
            infoPtr.pointed.toKFactoryInfo()
        }
    }

    @ExperimentalUnsignedTypes
    val classInfo: List<ClassInfo> by lazy {
        val nClass = IPluginFactory_countClasses(thisPtr)
        memScoped {
            val infoPtr = alloc<PClassInfo>().ptr
            (0 until nClass).map { i ->
                IPluginFactory_getClassInfo(thisPtr, i, infoPtr)
                infoPtr.pointed.toKClassInfo()
            }
        }.toList()
    }

    fun createIAudioProcessor(classID: UID): CPointer<IAudioProcessor> = memScoped {
        val interfaceID = IAudioProcessor_iid.toUID()
        createInstance(thisPtr, classID, interfaceID)
    }

    override fun close() {
        super.close()
        factory2Ptr?.let { IPluginFactory_release(it.reinterpret()) }
        factory3Ptr?.let { IPluginFactory_release(it.reinterpret()) }
    }

    fun createComponent(classID: UID): Component {
        return Component(memScoped {
            createInstance(thisPtr, classID, InterfaceID.IComponent.uid)
        })
    }

    fun createAudioProcessor(classID: UID): AudioProcessor {
        return AudioProcessor(memScoped {
            createInstance(thisPtr, classID, InterfaceID.IAudioProcessor.uid)
        })
    }

    fun createEditController(classID: UID): EditController {
        return EditController(memScoped {
            createInstance(thisPtr, classID, InterfaceID.IEditController.uid)
        })
    }

	private fun <S : CStructVar> AutofreeScope.createInstance(
		factory: CPointer<IPluginFactory>,
		classID: UID,
		interfaceID: UID
	): CPointer<S> {
		val cid = classID.toFuidPtr(this)
		val iid = interfaceID.toFuidPtr(this)
		val buf: CPointerVar<S> = alloc()
		val bufPtr: CPointer<COpaquePointerVar> = buf.ptr.reinterpret()
		val result = IPluginFactory_createInstance(factory, cid, iid, bufPtr)
		val interfacePtr = buf.value
		checkNotNull(interfacePtr) {
			log.warn { "Failed to createInstance (${result.kResultString})" }
			"${result.kResultString}: classID=$classID, interfaceID=$interfaceID"
		}
		log.info { "Success to createInstance (${InterfaceID.of(iid.pointed.tuid.toUID())})" }
		return interfacePtr
	}

    @ExperimentalUnsignedTypes
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

    @ExperimentalUnsignedTypes
    private fun PClassInfo.toKClassInfo(info2: PClassInfo2? = null, info3: PClassInfoW? = null): ClassInfo {
        return ClassInfo(
            classId = (info3?.cid ?: info2?.cid ?: cid).toUID(),
            cardinality = info3?.cardinality ?: info2?.cardinality ?: cardinality,
            category = (info3?.category ?: info2?.category ?: category).toKString().toVstClassCategory(),
            name = info3?.name?.toKStringFromUtf16() ?: (info2?.name ?: name).toKString(),
            subCategories = (info3?.subCategories ?: info2?.subCategories)?.toKString(),
            vendor = info3?.vendor?.toKStringFromUtf16() ?: info2?.vendor?.toKString(),
            version = info3?.version?.toKStringFromUtf16() ?: info2?.version?.toKString(),
            sdkVersion = info3?.sdkVersion?.toKStringFromUtf16() ?: info2?.sdkVersion?.toKString(),
            flags = (info3?.classFlags ?: info2?.classFlags)?.let { ClassInfo.Flags(it.toInt()) }
        )
    }

    private fun String.toVstClassCategory(): VstClassCategory = VstClassCategory.values().find { it.value == this }
        ?: throw IllegalArgumentException("$this is not VstCategory")
}