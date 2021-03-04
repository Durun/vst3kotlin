package io.github.durun.vst3kotlin.modules

import io.github.durun.io.Path
import io.github.durun.resource.Shared
import io.github.durun.vst3kotlin.hosting.AudioInstance
import io.github.durun.vst3kotlin.hosting.Module
import io.github.durun.vst3kotlin.pluginterface.base.ClassInfo
import io.github.durun.vst3kotlin.pluginterface.base.UID
import io.github.durun.vst3kotlin.pluginterface.vst.IoMode

interface VstClassRepository {
	val classInfos: Collection<ClassInfo>
	val classes: Collection<VstClass>
}

class VstClass(
	private val classId: UID,
	private val module: Shared<Module>
) {
	fun createAudioInstance(mode: IoMode = IoMode.Advanced): AudioInstance {
		return TODO() //AudioInstance.create(module.open(), classId, mode)
	}
}


class VstFile(path: Path) : VstClassRepository {
	private val lib: Shared<Module> = Shared(
		onOpen = { Module.open(path) },
		onClose = { it.close() }
	)
	override val classInfos: Collection<ClassInfo> = lib.use { it.factory.classInfo }
	override val classes: Collection<VstClass> = classInfos.map { VstClass(it.classId, lib) }
}
