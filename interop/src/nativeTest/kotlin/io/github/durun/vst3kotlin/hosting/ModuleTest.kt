package io.github.durun.vst3kotlin.hosting

import io.github.durun.resource.use
import io.github.durun.vst3kotlin.testResources
import kotlin.test.Test

@ExperimentalUnsignedTypes
class ModuleTest {
    @Test
    fun open_twice() {
        val path = testResources.resolve("vst3/again.vst3")
        // close automatically
        Module.of(path).use {
            println(it.factory)
        }
        // close manually
        val module = Module.of(path)
        println(module.factory)
        module.close()
    }
}