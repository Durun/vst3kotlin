package io.github.durun.vst3kotlin.base

import cwrapper.*
import io.github.durun.dylib.use
import io.github.durun.path.Path
import io.github.durun.vst3kotlin.Vst3Package
import io.github.durun.vst3kotlin.vst.ComponentHandler
import io.kotest.matchers.shouldBe
import kotlinx.cinterop.alloc
import kotlinx.cinterop.memScoped
import kotlinx.cinterop.ptr
import kotlinx.cinterop.reinterpret
import kotlin.test.Test

@kotlin.ExperimentalUnsignedTypes
class HostContextTest {
	val path = Path.of("src/commonTest/resources/vst3/again.vst3")

	@Test
	fun test() {
		Vst3Package.open(path).use { plugin ->
			plugin.openPluginFactory().use { factory ->
				val cid = factory.classInfo.first().classId

				memScoped {
					val s = alloc<SIComponentHandler>()
					val v = alloc<IComponentHandlerVTable>()
					s.vtable = v.ptr

					val context = HostContext(s.ptr)
					//val handler = ComponentHandler(s.ptr.reinterpret())
					val cHandler = s.ptr.reinterpret<IComponentHandler>()

					factory.createComponent(cid).use { component ->
						component.initialize(context)

						//handler.beginEdit(1u)
						//handler.beginEdit(5u)
						//handler.endEdit(5u)
						//handler.endEdit(1u)
						IComponentHandler_beginEdit(cHandler, 1u)
						IComponentHandler_beginEdit(cHandler, 5u)
						IComponentHandler_endEdit(cHandler, 5u)
						IComponentHandler_endEdit(cHandler, 1u)

						component.terminate()
					}

					val messages = context.receiveMessages()
					messages.forEach {
						println(it)
					}
					messages.size shouldBe 4
					(messages[0] as Message.BeginEdit).id shouldBe 1u
					(messages[1] as Message.BeginEdit).id shouldBe 5u
					(messages[2] as Message.EndEdit).id shouldBe 5u
					(messages[3] as Message.EndEdit).id shouldBe 1u
				}
			}
		}
	}
}