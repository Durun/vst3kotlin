package io.github.durun.vst3kotlin.vst

import io.github.durun.util.CClass
import io.github.durun.vst3kotlin.base.FUnknown

typealias AttrID = String

interface AttrReadProperty<T> {
	operator fun get(id: AttrID): T
}

interface AttrWriteProperty<T> {
	operator fun set(id: AttrID, value: T)
}

interface AttrProperty<T> : AttrReadProperty<T>, AttrWriteProperty<T>

expect class AttributeList : FUnknown, CClass {
	val int: AttrProperty<Long>
	val float: AttrProperty<Double>
	fun getString(id: AttrID, sizeInBytes: Int): String
	fun setString(id: AttrID, value: String)
	fun getBinary(id: AttrID, size: Int): ByteArray
	fun setBinary(id: AttrID, value: ByteArray)
}