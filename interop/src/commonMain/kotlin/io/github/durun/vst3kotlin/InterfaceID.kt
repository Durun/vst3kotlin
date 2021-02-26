package io.github.durun.vst3kotlin

import io.github.durun.vst3kotlin.base.UID

enum class InterfaceID(val uid: UID) {
	IPluginFactory(UID("7A4D811C52114A1FAED9D2EE0B43BF9F")),
	IPluginFactory2(UID("0007B650F24B4C0BA464EDB9F00B2ABB")),
	IPluginFactory3(UID("4555A2ABC1234E579B12291036878931")),
	IComponent(UID("E831FF31F2D54301928EBBEE25697802")),
	IEditController(UID("DCD7BBE37742448DA874AACC979C759E")),
	IEditController2(UID("7F4EFE59F3204967AC27A3AEAFB63038")),
	IConnectionPoint(UID("70A4156F6E6E4026989148BFAA60D8D1")),
	IBStream(UID("C3BF6EA2309947529B6BF9901EE33E9B")),
	ISizeableStream(UID("04F9549EE02F4E6E87E86A8747F4E17F")),
	IAudioProcessor(UID("42043F99B7DA453CA569E79D9AAEC33D")),
	IAudioPresentationLatency(UID("309ECE78EB7D4fae8B2225D909FD08B6")),
	IProcessContextRequirements(UID("2A654303EF764E3D95B5FE83730EF6D0")),
	IEventList(UID("3A2C4214346349FEB2C4F397B9695A44")),
	INoteExpressionController(UID("B7F8F85941234872911695814F3721A3")),
	IKeyswitchController(UID("1F2F76D3BFFB4B96B99527A55EBCCEF4")),
	IParamValueQueue(UID("01263A18ED074F6F98C9D3564686F9BA")),
	IParameterChanges(UID("A47796630BB64A56B44384A8466FEB9D"));

	override fun toString(): String = "${this::name}($uid)"

	companion object {
		fun of(uid: UID): InterfaceID? = values().firstOrNull { it.uid == uid }
        fun nameOf(uid: UID):String = of(uid)?.toString() ?: uid.toString()
	}
}