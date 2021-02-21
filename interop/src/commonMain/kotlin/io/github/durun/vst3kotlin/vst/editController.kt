package io.github.durun.vst3kotlin.vst

import io.github.durun.util.CClass
import io.github.durun.vst3kotlin.base.BStream
import io.github.durun.vst3kotlin.base.PluginBase
import io.github.durun.vst3kotlin.gui.PlugView

//typealias ParamID = UInt
//typealias UnitID = Int
//typealias ParamValue = Double

@kotlin.ExperimentalUnsignedTypes
data class ParameterInfo(
	val id: UInt,
	val title: String,
	val shortTitle: String,
	val units: String,
	val stepCount: Int,
	val defaultNormalizedValue: Double,
	val unitID: Int,
	val canAutomate: Boolean,
	val isReadOnly: Boolean,
	val isWrapAround: Boolean,
	val isList: Boolean,
	val isHidden: Boolean,
	val isProgramChange: Boolean,
	val isBypass: Boolean
) {
	constructor(
		id: UInt,
		title: String,
		shortTitle: String,
		units: String,
		stepCount: Int,
		defaultNormalizedValue: Double,
		unitID: Int,
		flags: Int
	) : this(
		id, title, shortTitle, units, stepCount, defaultNormalizedValue, unitID,
		canAutomate = flags and (1 shl 0) != 0,
		isReadOnly = flags and (1 shl 1) != 0,
		isWrapAround = flags and (1 shl 2) != 0,
		isList = flags and (1 shl 3) != 0,
		isHidden = flags and (1 shl 4) != 0,
		isProgramChange = flags and (1 shl 15) != 0,
		isBypass = flags and (1 shl 16) != 0
	)
}

enum class KnobMode(val value: Int) {
	CircularMode(0), RelativCircularMode(1), LinearMode(2)
}

expect class EditController : PluginBase, CClass {
	// version 1
	fun setComponentState(state: BStream)
	fun setState(state: BStream)
	val state: BStream

	@kotlin.ExperimentalUnsignedTypes
	val parameterInfo: List<ParameterInfo>

	@kotlin.ExperimentalUnsignedTypes
	fun getParamStringByValue(id: UInt, valueNormalized: Double): String

	@kotlin.ExperimentalUnsignedTypes
	fun getDoubleByString(id: UInt, string: String): Double

	@kotlin.ExperimentalUnsignedTypes
	fun normalizedParamToPlain(id: UInt, valueNormalized: Double): Double

	@kotlin.ExperimentalUnsignedTypes
	fun plainParamToNormalized(id: UInt, plainValue: Double): Double

	@kotlin.ExperimentalUnsignedTypes
	fun getParamNormalized(id: UInt): Double

	@kotlin.ExperimentalUnsignedTypes
	fun setParamNormalized(id: UInt, value: Double)
	fun setComponentHandler(handler: ComponentHandler)
	fun createView(name: String): PlugView

	// version 2
	fun setKnobMode(mode: KnobMode)
	fun openHelp(onlyCheck: Boolean)
	fun openAboutBox(onlyCheck: Boolean)
}