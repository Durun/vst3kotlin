package io.github.durun.vst3kotlin.vst

import cwrapper.*
import io.kotest.matchers.shouldBe
import kotlinx.cinterop.*
import kotlin.test.Test

class ParameterChangesTest {
    @Test
    fun testCFunctions() {
        // test data
        val paramID = 114u
        val offset = 5
        val gain = 0.9

        val params = allocIParameterChanges()
        memScoped {
            val index = alloc<IntVar>()
            val id = alloc<UIntVar>().apply { this.value = paramID }
            SIParameterChanges_addParameterData(params, id.ptr, index.ptr)
            SIParameterChanges_getParameterCount(params) shouldBe 1

            val queue = SIParameterChanges_getParameterData(params, index.value)!!
            queue.reinterpret<SIParamValueQueue>().pointed.apply {
                _pointCount shouldBe 0
                _id shouldBe paramID
            }

            val qIndex = alloc<IntVar>()
            SIParamValueQueue_addPoint(queue, offset, gain, qIndex.ptr)
            SIParamValueQueue_getPointCount(queue) shouldBe 1

            val outOffset = alloc<IntVar>()
            val outGain = alloc<ParamValueVar>()
            SIParamValueQueue_getPoint(queue, qIndex.value, outOffset.ptr, outGain.ptr)
            outOffset.value shouldBe offset
            outGain.value shouldBe gain
        }
    }

    private fun allocIParameterChanges(): CPointer<IParameterChanges> {
        val struct: CPointer<SIParameterChanges> = SIParameterChanges_alloc()
            ?: throw Exception("Failed to allocate SIParameterChanges")
        SIParameterChanges_init(struct)
        return struct.reinterpret()
    }
}