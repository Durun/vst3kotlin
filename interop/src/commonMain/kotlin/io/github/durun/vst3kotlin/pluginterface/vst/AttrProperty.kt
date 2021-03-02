package io.github.durun.vst3kotlin.pluginterface.vst

typealias AttrID = String

interface AttrReadProperty<T> {
    operator fun get(id: AttrID): T
}

interface AttrWriteProperty<T> {
    operator fun set(id: AttrID, value: T)
}

interface AttrProperty<T> : AttrReadProperty<T>, AttrWriteProperty<T>
