package io.github.durun.vst3kotlin

import io.github.durun.vst3kotlin.base.BStream
import io.github.durun.vst3kotlin.base.FUnknown
import io.github.durun.vst3kotlin.base.PluginFactory
import io.github.durun.vst3kotlin.base.UID
import io.github.durun.vst3kotlin.vst.AudioProcessor
import io.github.durun.vst3kotlin.vst.Component
import io.github.durun.vst3kotlin.vst.EditController

object InterfaceID {
	inline operator fun <reified I : FUnknown> get(version: Int = 1): UID = when (I::class) {
		PluginFactory::class -> when (version) {
			1 -> IPluginFactory
			2 -> IPluginFactory2
			3 -> IPluginFactory3
			else -> throw IllegalArgumentException("IPluginFactory version must be in 1-3 but: $version")
		}
		BStream::class -> IBStream
		AudioProcessor::class -> IAudioProcessor
		EditController::class -> IEditController
		Component::class -> IComponent
		else -> throw NoSuchElementException("No interface ID: ${I::class}")
	}

	val IPluginFactory = UID("7A4D811C52114A1FAED9D2EE0B43BF9F")
	val IPluginFactory2 = UID("0007B650F24B4C0BA464EDB9F00B2ABB")
	val IPluginFactory3 = UID("4555A2ABC1234E579B12291036878931")
	val IComponent = UID("E831FF31F2D54301928EBBEE25697802")
	val IEditController = UID("DCD7BBE37742448DA874AACC979C759E")
	val IEditController2 = UID("7F4EFE59F3204967AC27A3AEAFB63038")
	val IBStream = UID("C3BF6EA2309947529B6BF9901EE33E9B")
	val ISizeableStream = UID("04F9549EE02F4E6E87E86A8747F4E17F")
	val IAudioProcessor = UID("42043F99B7DA453CA569E79D9AAEC33D")
	val IAudioPresentationLatency = UID("309ECE78EB7D4fae8B2225D909FD08B6")
	val IProcessContextRequirements = UID("2A654303EF764E3D95B5FE83730EF6D0")
	val IEventList = UID("3A2C4214346349FEB2C4F397B9695A44")
	val INoteExpressionController = UID("B7F8F85941234872911695814F3721A3")
	val IKeyswitchController = UID("1F2F76D3BFFB4B96B99527A55EBCCEF4")
	val IParamValueQueue = UID("01263A18ED074F6F98C9D3564686F9BA")
	val IParameterChanges = UID("A47796630BB64A56B44384A8466FEB9D")
}