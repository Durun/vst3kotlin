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
        SIParameterChanges_free(params.reinterpret())
    }

    @Test
    fun testViaCInterface() {
        // test data
        val paramID = 114u
        val offset = 5
        val gain = 0.9

        val params = allocIParameterChanges()
        memScoped {
            val index = alloc<IntVar>()
            val id = alloc<UIntVar>().apply { this.value = paramID }
            IParameterChanges_addParameterData(params, id.ptr, index.ptr)

            val queue = IParameterChanges_getParameterData(params, index.value)!!

            val qIndex = alloc<IntVar>()
            IParamValueQueue_addPoint(queue, offset, gain, qIndex.ptr)

            val outOffset = alloc<IntVar>()
            val outGain = alloc<ParamValueVar>()
            IParamValueQueue_getPoint(queue, qIndex.value, outOffset.ptr, outGain.ptr)
        }

        // read data
        println("getParameterCount...")
        IParameterChanges_getParameterCount(params) shouldBe 1
        println("getParameterData...")
        val queue = IParameterChanges_getParameterData(params, 0)
        checkNotNull(queue)
        println("getPointCount...")
        IParamValueQueue_getPointCount(queue) shouldBe 1
        println("getParameterId...")
        IParamValueQueue_getParameterId(queue) shouldBe paramID
        memScoped {
            val outOffset = alloc<IntVar>()
            val outGain = alloc<ParamValueVar>()
            println("getPoint...")
            IParamValueQueue_getPoint(queue, 0, outOffset.ptr, outGain.ptr)
            outOffset.value shouldBe offset
            outGain.value shouldBe gain
        }
        FUnknown_release(params.reinterpret())
    }

    private fun allocIParameterChanges(): CPointer<IParameterChanges> {
        val struct: CPointer<SIParameterChanges> = SIParameterChanges_alloc()
            ?: throw Exception("Failed to allocate SIParameterChanges")
        SIParameterChanges_init(struct)
        return struct.reinterpret()
    }
}