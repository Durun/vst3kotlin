package io.github.durun.vst3kotlin

import io.github.durun.io.use
import io.github.durun.path.Path
import io.github.durun.vst3kotlin.base.VstClassCategory
import io.github.durun.vst3kotlin.cppinterface.HostCallback
import io.github.durun.vst3kotlin.gui.PlatformView
import io.github.durun.vst3kotlin.hosting.Module
import io.github.durun.window.Window
import io.github.durun.window.WindowClass
import platform.windows.WM_QUIT
import kotlin.test.Test

class WindowTest {
    //@Test
    fun window() {
        val window = Window.create("Hello", WindowClass.Simple)
        window.resize(480, 320)
        window.show()
        do {
            val msg = Window.getMessageForAll()
        } while (msg.message.toInt() != WM_QUIT)
    }

    //@Test
    fun vstWindow() {
        val path = Path.of("C:\\Program Files\\Common Files\\VST3\\FabFilter Pro-Q 3.vst3") //testResources.resolve("vst3/again.vst3")


        val window = Window.create("VST3", WindowClass.Vst(null))

        Module.of(path).use { module ->
            module.classes.filter { it.info.category == VstClassCategory.AudioEffect }
                .map { it.createInstance() }.forEach {instance->
                    instance.plugView?.apply {
                        val width = size.right - size.left
                        val height = size.bottom - size.top
                        window.resize(width, height)
                        println("resized: $width*$height")
                        attached(PlatformView(window.hwnd), "HWND")
                        println("attached.")
                    } ?: println("No PlugView.")
                    window.show()
                    loop()
                }
        }

    }


    private fun loop() {
        do {
            val msg = Window.getMessageForAll()

            HostCallback.receiveMessages()
                .forEach {
                    println(it)
                }

        } while (msg.message.toInt() != WM_QUIT)
    }
}