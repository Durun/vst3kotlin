package io.github.durun.vst3kotlin.vst

import cwrapper.*
import io.github.durun.vst3kotlin.base.FUnknown
import kotlinx.cinterop.*

/**
 * Representation of single parameter change sequence in one process frame.
 * The [keys] mean "sample offset" and the [values] mean [ParamValue]
 */
interface SingleParameterChange : Map<Int, ParamValue>

/**
 * Representation of parameter changes in one [AudioProcessor].
 * The [keys] mean [ParamID] and the [values] are [SingleParameterChange]
 */
interface ParameterChanges : Map<ParamID, SingleParameterChange> {
	private fun buildStruct(placement: NativePlacement): SIParameterChanges {
		val params: SIParameterChanges = placement.alloc()
		params.vtable = IParameterChangesVTable_def.ptr
		params.refCount = 1
		params.maxParams = this.size
		params._paramCount = this.size
		params._params = placement.allocArray(params.maxParams)
		entries.forEachIndexed { i, (paramId, values) ->
			val param = params._params!![i]
			param.vtable = IParamValueQueueVTable_def.ptr
			param.refCount = 1
			param.maxPoints = values.size
			param._pointCount = values.size
			param._id = paramId
			param._sampleOffset = placement.allocArray(param.maxPoints)
			param._value = placement.allocArray(param.maxPoints)
			values.entries.forEachIndexed { k, (offset, value) ->
				param._sampleOffset!![k] = offset
				param._value!![k] = value
			}
		}
		return params
	}

	fun placeToCInterface(placement: NativePlacement): CPointer<IParameterChanges> {
		return buildStruct(placement).reinterpret()
	}
}

// Kotlin style implementation
/**
 * @sample io.github.durun.vst3kotlin.vst.ParameterChangesTest.writeViaKotlinInterface
 */
fun Map<ParamID, Map<Int, ParamValue>>.toParameterChanges(): ParameterChanges {
	val data = this.mapValues { (_, v) -> HashSingleParameterChange(v) }
	return HashParameterChanges(data)
}

private class HashSingleParameterChange(
	private val data: Map<Int, ParamValue>
) : SingleParameterChange, Map<Int, ParamValue> by data

private class HashParameterChanges(
	private val data: Map<ParamID, SingleParameterChange>
) : ParameterChanges, Map<ParamID, SingleParameterChange> by data

// C style implementation
/**
 *
 */
fun allocIParameterChanges(maxParams: Int, maxPointsPerFrame: Int): CPointer<IParameterChanges> {
	return SIParameterChanges_alloc(maxParams, maxPointsPerFrame)
		?.also { SIParameterChanges_init(it) }
		?.reinterpret()
		?: throw Exception("Failed to alloc SIParameterChanges")
}

/**
 * @sample io.github.durun.vst3kotlin.vst.ParameterChangesTest.readViaKotlinInterface
 */
fun CPointer<IParameterChanges>.toKInterface(): ParameterChanges {
	return StructParameterChanges(this)
}

private class StructSingleParameterChange(
	private val ptr: CPointer<IParamValueQueue>
) : SingleParameterChange {
	override val size: Int
		get() = IParamValueQueue_getPointCount(ptr)

	fun toMap(): Map<Int, ParamValue> = memScoped {
		val offset: IntVar = alloc()
		val value: ParamValueVar = alloc()
		(0 until size).associate {
			IParamValueQueue_getPoint(ptr, it, offset.ptr, value.ptr)
			offset.value to value.value
		}
	}

	override val entries: Set<Map.Entry<Int, ParamValue>>
		get() = toMap().entries
	override val keys: Set<Int>
		get() = toMap().keys
	override val values: Collection<ParamValue>
		get() = toMap().values

	override fun containsKey(key: Int): Boolean = keys.contains(key)
	override fun containsValue(value: ParamValue): Boolean = values.contains(value)
	override fun isEmpty(): Boolean = size <= 0
	override fun get(key: Int): ParamValue? {
		return entries.find { (id, _) -> id == key }?.value
	}
}

private class StructParameterChanges(
	override val ptr: CPointer<IParameterChanges>
) : ParameterChanges, FUnknown() {
	override fun close() {
		SIParameterChanges_free(ptr.reinterpret())
		super.close()
	}

	override val size: Int
		get() = IParameterChanges_getParameterCount(ptr)

	private fun datas() = (0 until size).map { IParameterChanges_getParameterData(ptr, it)!! }
	fun toMap(): Map<ParamID, SingleParameterChange> {
		return datas().associate {
			val id = IParamValueQueue_getParameterId(it)
			id to StructSingleParameterChange(it)
		}
	}

	override val entries: Set<Map.Entry<ParamID, SingleParameterChange>>
		get() = toMap().entries
	override val keys: Set<ParamID>
		get() = datas().map { IParamValueQueue_getParameterId(it) }.toSet()
	override val values: Collection<SingleParameterChange>
		get() = toMap().values

	override fun containsKey(key: ParamID): Boolean = keys.contains(key)
	override fun containsValue(value: SingleParameterChange): Boolean = values.contains(value)
	override fun isEmpty(): Boolean = size <= 0
	override fun get(key: ParamID): SingleParameterChange? {
		return entries.find { (id, _) -> id == key }?.value
	}
}