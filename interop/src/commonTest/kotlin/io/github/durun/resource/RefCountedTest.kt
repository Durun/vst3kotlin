package io.github.durun.resource

import io.kotest.assertions.throwables.shouldThrowAny
import io.kotest.matchers.shouldBe
import kotlin.native.concurrent.ThreadLocal
import kotlin.test.Test

class RefCountedTest {
    @Test
    fun openAndClose() {
        val ref = Shared(onOpen = { SampleResource() }, onClose = { it.close() })

        ref.open().id shouldBe 0
        ref.close()

        ref.open().id shouldBe 1
        ref.use { it.id shouldBe 1 }
        ref.close()

        ref.use { it.id shouldBe 2 }
    }

    @Test
    fun closeTwiceCausesError() {
        val ref = Shared(onOpen = { SampleResource() }, onClose = { it.close() })
        ref.use { println(it) }
        shouldThrowAny {
            ref.close()
        }
    }
}

class SampleResource {
    @ThreadLocal
    companion object {
        var count = 0
    }

    val id = count++
    var isOpen = true
    fun close() {
        isOpen shouldBe true
        isOpen = false
    }
}