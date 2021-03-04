package io.github.durun.vst3kotlin.hosting

import io.github.durun.resource.Shared
import io.github.durun.vst3kotlin.pluginterface.base.UID
import io.github.durun.vst3kotlin.pluginterface.vst.IoMode

/**
 * VSTプラグイン1種類を表現するクラスです。
 * VSTプラグインインスタンス[AudioInstance], [ControllerInstance]のファクトリです。
 */
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