package io.github.durun.vst3kotlin.gui

import io.github.durun.io.use
import io.github.durun.util.Vec2
import io.github.durun.vst3kotlin.base.VstClassCategory
import io.github.durun.vst3kotlin.cppinterface.HostCallback
import io.github.durun.vst3kotlin.hosting.Module
import io.github.durun.vst3kotlin.testResources
import io.github.durun.vst3kotlin.window.Window
import io.github.durun.vst3kotlin.window.WindowEvent
import kotlin.test.Test

class WindowTest {
    //@Test
    fun vstWindow() {
        val path = testResources.resolve("vst3/again.vst3")

        val window = Window.create(Vec2(480, 320), "VST3")

        Module.of(path).use { module ->
            module.classes.find { it.info.category == VstClassCategory.AudioEffect }!!.createInstance()
                .use { instance ->
                    val view = instance.plugView ?: error("No PlugView.")
                    view.attached(window)
                    println("attached.")

                    window.show()

                    window.loop {
                        HostCallback.receiveMessages().forEach { println(it) }
                        it != WindowEvent.OnClosed
                    }
                }
        }
    }
}