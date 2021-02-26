package io.github.durun.vst3kotlin

import cwrapper.kPlatformTypeHWND
import io.github.durun.io.use
import io.github.durun.path.Path
import io.github.durun.vst3kotlin.base.VstClassCategory
import io.github.durun.vst3kotlin.gui.PlatformView
import io.github.durun.vst3kotlin.hosting.Module
import io.github.durun.vst3kotlin.hosting.PluginInstance
import io.github.durun.window.Window
import io.github.durun.window.WindowClass
import platform.windows.WM_QUIT
import kotlin.test.Test

class WindowTest {
    @Test
    fun window() {
        val window = Window.create("Hello", WindowClass.Simple)
        window.resize(480, 320)
        window.show()
        do {
            val msg = Window.getMessageForAll()
        } while (msg.message.toInt() != WM_QUIT)
    }

    @Test
    fun vstWindow() {
        val path = Path.of("C:\\Program Files\\Common Files\\VST3\\FabFilter Pro-Q 3.vst3") //testResources.resolve("vst3/again.vst3")


        val window = Window.create("VST3", WindowClass.Vst(null))

        Module.of(path).use { module ->
            val clazz = module.factory.classInfo.find { it.category == VstClassCategory.AudioEffect }
                ?: throw NoSuchElementException("no Audio Effect Class")
            PluginInstance.create(module.factory, clazz.classId).use {
                it.plugView?.apply {
                    println(size)
                    val width = size.left - size.right
                    val height = size.top - size.bottom
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
        } while (msg.message.toInt() != WM_QUIT)
    }
}