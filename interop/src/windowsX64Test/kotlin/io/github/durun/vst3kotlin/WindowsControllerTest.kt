package io.github.durun.vst3kotlin

import io.github.durun.io.Path
import io.github.durun.resource.use
import io.github.durun.vst3kotlin.cppinterface.HostCallback
import io.github.durun.vst3kotlin.hosting.ControllerInstance
import io.github.durun.vst3kotlin.hosting.Module
import io.github.durun.vst3kotlin.pluginterface.base.VstClassCategory
import io.github.durun.vst3kotlin.window.Window
import io.github.durun.vst3kotlin.window.WindowClass
import platform.windows.WM_QUIT
import kotlin.test.Test

class WindowsControllerTest {
    val path =
        Path.of("C:\\Program Files\\Common Files\\VST3\\FabFilter Pro-Q 3.vst3") //testResources.resolve("vst3/again.vst3")

    //@Test
    fun test() {

        val window = Window.create("VST3", WindowClass.Vst(null))

        Module.of(path).use { module ->
            println("Module open.")
            val classInfo = module.factory.classInfo.first { it.category == VstClassCategory.AudioEffect }
            ControllerInstance.create(module.factory, classInfo.classId).use {
                println("ControllerInstance open.")
                it.plugView?.attached(window)
                window.show()
                loop()
            }
            println("ControllerInstance closed.")
        }
        println("Module closed.")
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