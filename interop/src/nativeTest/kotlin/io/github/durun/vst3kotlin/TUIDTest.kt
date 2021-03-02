package io.github.durun.vst3kotlin

import cwrapper.TUID_from4Int
import io.github.durun.vst3kotlin.base.toUID
import io.github.durun.vst3kotlin.pluginterface.base.UID
import io.kotest.matchers.shouldBe
import kotlinx.cinterop.ByteVar
import kotlinx.cinterop.allocArray
import kotlinx.cinterop.memScoped
import kotlin.test.Test

class TUIDTest {
	@Test
	fun encodeAndDecodeTUID() {
		val idFromC = memScoped {
			val tuid = allocArray<ByteVar>(16)
			TUID_from4Int(tuid, 0x01234567, 0x89ABCDEF, 0x01234567, 0x89ABCDEF)
			tuid.toUID()
		}
		idFromC shouldBe UID("0123456789ABCDEF0123456789ABCDEF")
	}
}