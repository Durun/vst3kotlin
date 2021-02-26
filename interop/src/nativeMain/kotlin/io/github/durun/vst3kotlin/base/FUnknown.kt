package io.github.durun.vst3kotlin.base

import cwrapper.*
import cwrapper.FUnknown
import io.github.durun.io.Closeable
import io.github.durun.log.logger
import io.github.durun.vst3kotlin.InterfaceID
import io.github.durun.vst3kotlin.VstInterface
import io.github.durun.vst3kotlin.cppinterface.CClass
import kotlinx.cinterop.*

abstract class FUnknown : Closeable, CClass {
    private val thisPtr: CPointer<FUnknown> get() = ptr.reinterpret()
    final override var isOpen: Boolean = true
        private set
    private val log by logger()

    override fun close() {
        check(isOpen)
        FUnknown_release(thisPtr)
        isOpen = false
    }

    fun queryInterface(interfaceID: TUID): CPointer<*> {
        return memScoped {
            val obj = alloc<CPointerVar<*>>()
            val result = IPluginFactory_queryInterface( // TODO: use FUnknown_queryInterface
                thisPtr.reinterpret(),
                interfaceID,
                obj.ptr
            )
            check(result == kResultTrue) {
                when (result) {
                    kNoInterface -> {
                        val uid = interfaceID.toUID()
                        "NoInterface: ${InterfaceID.nameOf(uid)}"
                            .also { log.warn { "Failed to queryInterface: $it" } }
                    }
                    else ->  {
                        result.kResultString
                            .also { log.error { "Failed to queryInterface: $it" } }
                    }
                }
            }
            val ptr = obj.value
            checkNotNull(ptr)
            ptr
        }
    }

    fun <I : CPointed> queryVstInterface(interfaceID: TUID): VstInterface<CPointer<I>> {
        val ptr = queryInterface(interfaceID)
            .reinterpret<I>()
        return VstInterface(ptr)
    }
}