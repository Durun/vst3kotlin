package io.github.durun.vst3kotlin.vst

import io.github.durun.vst3kotlin.base.BStream
import io.github.durun.vst3kotlin.base.FUnknown
import io.github.durun.vst3kotlin.gui.PlugView

@OptIn(ExperimentalUnsignedTypes::class)
public typealias ParamID = UInt
public typealias UnitID = Int
public typealias ParamValue = Double

data class ParameterInfo(
	val id: ParamID,
	val title: String,
	val shortTitle: String,
	val units: String,
	val stepCount: Int,
	val defaultNormalizedValue: ParamValue,
	val unitID: UnitID,
	val canAutomate: Boolean,
	val isReadOnly: Boolean,
	val isWrapAround: Boolean,
	val isList: Boolean,
	val isHidden: Boolean,
	val isProgramChange: Boolean,
	val isBypass: Boolean
) {
	constructor(
		id: ParamID,
		title: String,
		shortTitle: String,
		units: String,
		stepCount: Int,
		defaultNormalizedValue: ParamValue,
		unitID: UnitID,
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
	fun beginEdit(id: ParamID)
	fun performEdit(id: ParamID, valueNormalized: ParamValue)
	fun endEdit(id: ParamID)
	fun restartComponent(flags: Int)

	fun setDirty(state: Boolean)
	fun requestOpenEditor(name: String)
	fun startGroupEdit()
	fun finishGroupEdit()
}

expect class EditController : FUnknown {
	fun setComponentState(state: BStream)
	fun setState(state: BStream)
	val state: BStream
	val parameterInfo: List<ParameterInfo>
	fun getParamStringByValue(id: ParamID, valueNormalized: ParamValue): String
	fun getParamValueByString(id: ParamID, string: String): ParamValue
	fun normalizedParamToPlain(id: ParamID, valueNormalized: ParamValue): ParamValue
	fun plainParamToNormalized(id: ParamID, plainValue: ParamValue): ParamValue
	fun getParamNormalized(id: ParamID): ParamValue
	fun setParamNormalized(id: ParamID, value: ParamValue)
	fun setComponentHandler(handler: ComponentHandler)
	fun createView(name: String): PlugView
}