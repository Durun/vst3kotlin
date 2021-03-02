package io.github.durun.util

data class Vec2<T>(val x: T, val y: T) {
	fun <R> map(transform: (T) -> R): Vec2<R> {
		return Vec2(transform(x), transform(y))
	}
}

fun Vec2<Int>.toUInt(): Vec2<UInt> = map { it.toUInt() }