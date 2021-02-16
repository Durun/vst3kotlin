package io.github.durun.vst3kotlin

import cwrapper.FUID
import cwrapper.FUID_from4Int
import cwrapper.FUID_fromTUID
import cwrapper.TUID_from4Int
import io.github.durun.vst3kotlin.base.TUID
import io.github.durun.vst3kotlin.base.toTUID
import io.kotest.matchers.shouldBe
import kotlinx.cinterop.*
import kotlin.test.Test

class FUIDTest {
	@Test
	fun from4Int() {
		val idFromC = memScoped {
			val fuid = alloc<FUID>()
			FUID_from4Int(fuid.ptr, 0x01234567, 0x89ABCDEF, 0x01234567, 0x89ABCDEF)
			fuid.tuid.toTUID()
		}
		idFromC shouldBe TUID("0123456789ABCDEF0123456789ABCDEF")
	}

	@Test
	fun fromTUID() {
		val idFromC = memScoped {
			val tuid = allocArray<ByteVar>(16)
			TUID_from4Int(tuid, 0x01234567, 0x89ABCDEF, 0x01234567, 0x89ABCDEF)
			val fuid = alloc<FUID>()
			FUID_fromTUID(fuid.ptr, tuid)
			fuid.tuid.toTUID()
		}
		idFromC shouldBe TUID("0123456789ABCDEF0123456789ABCDEF")
	}
}