package io.github.durun.vst3kotlin.cppinterface

import io.github.durun.dylib.use
import io.github.durun.vst3kotlin.vst.ComponentHandler
import io.kotest.matchers.shouldBe
import kotlinx.cinterop.ptr
import kotlin.test.Test

@kotlin.ExperimentalUnsignedTypes
class HostCallbackTest {
	@Test
	fun call_as_IComponentHandler() {
		val ptr = HostCallback.cClass.ptr

		println("open IComponentHandler")
		ComponentHandler(ptr).use {
			it.beginEdit(1u)
			it.performEdit(1u, 0.5)
			it.endEdit(1u)
			it.restartComponent(-114514)
		}
		println("Closed IComponentHandler")

		val messages = HostCallback.receiveMessages().toList()
			.onEach { println(it) }

		messages.map(Message::toString) shouldBe listOf(
			Message.BeginEdit.of(1u),
			Message.PerformEdit.of(1u, 0.5),
			Message.EndEdit.of(1u),
			Message.RestartComponent.of(-114514)
		).map(Message::toString)
	}
}