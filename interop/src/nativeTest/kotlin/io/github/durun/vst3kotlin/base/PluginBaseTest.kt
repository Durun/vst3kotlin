package io.github.durun.vst3kotlin.base

import io.github.durun.resource.use
import io.github.durun.vst3kotlin.cppinterface.HostCallback
import io.github.durun.vst3kotlin.hosting.Module
import io.github.durun.vst3kotlin.testResources
import kotlin.test.Test

class PluginBaseTest {
    val path = testResources.resolve("vst3/again.vst3")

    @Test
    fun initialize() {
        Module.of(path).use { plugin ->
            val cid = plugin.factory.classInfo.first().classId
            plugin.factory.createComponent(cid).use { component ->
                component.initialize(HostCallback)
                component.terminate()
            }
        }
    }
}