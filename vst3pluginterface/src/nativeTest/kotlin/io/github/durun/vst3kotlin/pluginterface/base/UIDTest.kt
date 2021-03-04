package io.github.durun.vst3kotlin.pluginterface.base

import cwrapper.FUID
import cwrapper.FUID_from4Int
import cwrapper.FUID_fromTUID
import cwrapper.TUID_from4Int
import io.kotest.matchers.shouldBe
import kotlinx.cinterop.*
import kotlin.test.Test

class UIDTest {
	@Test
	fun from4Int() {
		val idFromC = memScoped {
			val fuid = alloc<FUID>()
			FUID_from4Int(fuid.ptr, 0x01234567, 0x89ABCDEF, 0x01234567, 0x89ABCDEF)
			fuid.tuid.toUID()
		}
		idFromC shouldBe UID("0123456789ABCDEF0123456789ABCDEF")
	}

	@Test
	fun fromTUID() {
		val idFromC = memScoped {
			val tuid = allocArray<ByteVar>(16)
			TUID_from4Int(tuid, 0x01234567, 0x89ABCDEF, 0x01234567, 0x89ABCDEF)
			val fuid = alloc<FUID>()
			FUID_fromTUID(fuid.ptr, tuid)
			fuid.tuid.toUID()
		}
		idFromC shouldBe UID("0123456789ABCDEF0123456789ABCDEF")
	}

	@Test
	fun toFuidPtr() {
		val uid = UID("0123456789ABCDEF0123456789ABCDEF")
		memScoped {
			val fuid = alloc<FUID>()
			FUID_from4Int(fuid.ptr, 0x01234567, 0x89ABCDEF, 0x01234567, 0x89ABCDEF)
			uid.toFuidPtr(this).readBytes(24) shouldBe fuid.ptr.readBytes(24)
		}
	}

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