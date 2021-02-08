package io.github.durun.dylib

import io.kotest.matchers.shouldBe
import kotlinx.cinterop.*
import kotlin.test.Test

class DylibTest {
	@Test
	fun use_libc() {
		useDylib("libc.so.6") {
			val abs = getFunction<(Int) -> Int>("abs")
			abs(-1) shouldBe 1

			val atoi = getFunction<(CPointer<ByteVar>) -> Int>("atoi")
			memScoped { atoi("-255".utf8.ptr) } shouldBe -255
		}
	}
}