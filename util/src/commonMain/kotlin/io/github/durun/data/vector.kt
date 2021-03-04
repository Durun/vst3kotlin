package io.github.durun.data

data class Vec2<T>(val x: T, val y: T) {
	fun <R> map(transform: (T) -> R): Vec2<R> {
		3.minus(2)
		return Vec2(transform(x), transform(y))
	}
}

operator fun Vec2<Int>.minus(other: Vec2<Int>): Vec2<Int> = Vec2(this.x - other.x, this.y - other.y)

@kotlin.ExperimentalUnsignedTypes
operator fun Vec2<UInt>.minus(other: Vec2<UInt>): Vec2<UInt> = Vec2(this.x - other.x, this.y - other.y)

@kotlin.ExperimentalUnsignedTypes
fun Vec2<Int>.toUInt(): Vec2<UInt> = map { it.toUInt() }


data class IntBox(val leftTop: Vec2<Int>, val rightBottom: Vec2<Int>) {
	constructor(left: Int, top: Int, right: Int, bottom: Int) : this(Vec2(left, top), Vec2(right, bottom))

	val size: Vec2<Int> = rightBottom - leftTop
	val left: Int get() = leftTop.x
	val top: Int get() = leftTop.y
	val right: Int get() = rightBottom.x
	val bottom: Int get() = rightBottom.y
}