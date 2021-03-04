package io.github.durun.data

@kotlin.ExperimentalUnsignedTypes
fun UByte.toBoolean(): Boolean = this != (0u).toUByte()