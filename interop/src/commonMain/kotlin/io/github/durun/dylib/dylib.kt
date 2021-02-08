package io.github.durun.dylib


expect class DylibScope

expect fun <T> useDylib(lib: String, body: DylibScope.() -> T?): T?