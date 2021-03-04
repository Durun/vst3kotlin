package io.github.durun.vst3kotlin

import io.github.durun.data.Vec2
import io.github.durun.resource.use
import io.github.durun.vst3kotlin.cppinterface.HostCallback
import io.github.durun.vst3kotlin.hosting.Module
import io.github.durun.vst3kotlin.hosting.VstClassRepository
import io.github.durun.vst3kotlin.hosting.VstFile
import io.github.durun.vst3kotlin.pluginterface.base.VstClassCategory
import io.github.durun.window.Window
import io.github.durun.window.WindowEvent
import kotlin.test.Test

class WindowTest {
    //@Test
    fun vstWindow() {
        val path = testResources.resolve("vst3/TAL-NoiseMaker.vst3")

        val window = Window.create(Vec2(480, 320), "VST3")

        val repo: VstClassRepository = VstFile.of(path)
        val classId = repo.classInfos.first { it.category == VstClassCategory.AudioEffect }.classId
        val vstClass = repo[classId] ?: error("No Audio effect")
        vstClass.createControllerInstance().use { instance ->
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