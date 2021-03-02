package io.github.durun.vst3kotlin.cppinterface

import io.kotest.matchers.shouldBe
import kotlinx.cinterop.memScoped
import kotlinx.cinterop.nativeHeap
import kotlin.test.Test

class LongArrayStoreTest {
	val size = 256
	val testData = listOf<Long>(0xABC, 0xEF)

	@Test
	fun onHeap() {
		val store = allocLongArrayStore(nativeHeap, size)

		store.use {
			it[0] = testData[0]
			it[size - 1] = testData[1]
		}

		store.use {
			it[0] shouldBe testData[0]
			it[size - 1] shouldBe testData[1]
		}

		store.free()
	}

	@Test
	fun onStack() {
		memScoped {
			val store = allocLongArrayStore(this, size)

			store.use {
				it[0] = testData[0]
				it[size - 1] = testData[1]
			}

			store.use {
				it[0] shouldBe testData[0]
				it[size - 1] shouldBe testData[1]
			}
		}
	}
}