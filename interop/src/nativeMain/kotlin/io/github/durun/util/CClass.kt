package io.github.durun.util

import kotlinx.cinterop.CPointer

actual interface CClass {
	val ptr: CPointer<*>
}