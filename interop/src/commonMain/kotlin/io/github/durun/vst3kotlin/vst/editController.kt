package io.github.durun.vst3kotlin.vst

import io.github.durun.vst3kotlin.base.BStream
import io.github.durun.vst3kotlin.base.FUnknown
import io.github.durun.vst3kotlin.base.PluginBase
import io.github.durun.vst3kotlin.gui.PlugView

//typealias ParamID = UInt
//typealias UnitID = Int
//typealias ParamValue = Double

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

expect class ComponentHandler:FUnknown {
	fun beginEdit(id: UInt)
	fun performEdit(id: UInt, valueNormalized: Double)
	fun endEdit(id: UInt)
	fun restartComponent(flags: Int)

	fun setDirty(state: Boolean)
	fun requestOpenEditor(name: String)
	fun startGroupEdit()
	fun finishGroupEdit()
}

expect class EditController : PluginBase {
	fun setComponentState(state: BStream)
	fun setState(state: BStream)
	val state: BStream
	val parameterInfo: List<ParameterInfo>
	fun getParamStringByValue(id: UInt, valueNormalized: Double): String
	fun getDoubleByString(id: UInt, string: String): Double
	fun normalizedParamToPlain(id: UInt, valueNormalized: Double): Double
	fun plainParamToNormalized(id: UInt, plainValue: Double): Double
	fun getParamNormalized(id: UInt): Double
	fun setParamNormalized(id: UInt, value: Double)
	fun setComponentHandler(handler: ComponentHandler)
	fun createView(name: String): PlugView
}