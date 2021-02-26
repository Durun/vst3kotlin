package io.github.durun.dylib

import io.github.durun.io.use
import io.kotest.matchers.shouldBe
import kotlinx.cinterop.*
import kotlin.test.Test

class DylibTest {
	@Test
	fun openAndClose() {
		val lib = Dylib.open("libc.so.6")
		val abs = lib.getFunction<(Int) -> Int>("abs")
		abs(-1) shouldBe 1
		val atoi = lib.getFunction<(CPointer<ByteVar>) -> Int>("atoi")
		memScoped { atoi("-255".utf8.ptr) } shouldBe -255
		lib.close()
	}

	@Test
	fun autoClose() {
		Dylib.open("libc.so.6").use {
			val abs = it.getFunction<(Int) -> Int>("abs")
			abs(-1) shouldBe 1
			val atoi = it.getFunction<(CPointer<ByteVar>) -> Int>("atoi")
			memScoped { atoi("-255".utf8.ptr) }
		} shouldBe -255
	}
}