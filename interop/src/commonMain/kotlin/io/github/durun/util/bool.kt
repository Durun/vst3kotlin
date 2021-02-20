package io.github.durun.util

@kotlin.ExperimentalUnsignedTypes
fun UByte.toBoolean(): Boolean = this != (0u).toUByte()