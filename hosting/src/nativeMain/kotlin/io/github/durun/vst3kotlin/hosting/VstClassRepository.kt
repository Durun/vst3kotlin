package io.github.durun.vst3kotlin.hosting

import io.github.durun.io.Path
import io.github.durun.util.logger
import io.github.durun.resource.Shared
import io.github.durun.vst3kotlin.pluginterface.base.ClassInfo
import io.github.durun.vst3kotlin.pluginterface.base.UID
import io.github.durun.vst3kotlin.pluginterface.vst.IoMode

interface VstClassRepository {
    val classInfos: Collection<ClassInfo>
    operator fun get(classId: UID): VstClass?
}

class VstFile(path: Path) : VstClassRepository {
    private val log by logger()

    /**
     * [VstFile] は直接 VST Plugin をCloseしない。
     * [AudioInstance], [ControllerInstance] が Shared<Module>.close()を呼んだ時にCloseされる。
     */
    private val lib: Shared<Module> = Shared(
        onOpen = {
            Module.open(path).also { log.info { "Opened module $path" } }
        },
        onClose = {
            it.close()
            log.info { "Closed module $path" }
        }
    )
    override val classInfos: Collection<ClassInfo> = lib.use { it.factory.classInfo }
    private val classes: Map<UID, VstClass> = classInfos.associate { it.classId to VstClass(it.classId, lib) }
    override operator fun get(classId: UID): VstClass? {
        return classes[classId]
    }

    companion object {
        fun of(path: Path): VstFile {
            return VstFile(path)
        }
    }
}

class VstClass(
    private val classId: UID,
    private val module: Shared<Module>
) {
    fun createAudioInstance(mode: IoMode = IoMode.Advanced): AudioInstance {
        return AudioInstance.create(
            factory = module.open().factory,
            classID = classId,
            ioMode = mode,
            toClose = module
        )
    }

    fun createControllerInstance(): ControllerInstance {
        return ControllerInstance.create(
            factory = module.open().factory,
            classID = classId,
            toClose = module
        )
    }
}


