package io.github.durun.vst3kotlin.vst

import cwrapper.ParamID
import cwrapper.ParamValue

/**
 * Representation of single parameter change sequence in one process frame.
 * The [keys] mean "sample offset" and the [values] mean [ParamValue]
 */
interface MutableSingleParameterChange : SingleParameterChange, MutableMap<Int, ParamValue>
interface SingleParameterChange : Map<Int, ParamValue>

/**
 * Representation of parameter changes in one [AudioProcessor].
 * The [keys] mean [ParamID] and the [values] are [SingleParameterChange]
 */
interface MutableParameterChanges : ParameterChanges, MutableMap<ParamID, SingleParameterChange>
interface ParameterChanges : Map<ParamID, SingleParameterChange>

class HashSingleParameterChange(
	private val data: MutableMap<Int, ParamValue>
) : MutableSingleParameterChange, MutableMap<Int, ParamValue> by data

class HashParameterChanges(
	private val data: MutableMap<ParamID, SingleParameterChange>
) : MutableParameterChanges, MutableMap<ParamID, SingleParameterChange> by data