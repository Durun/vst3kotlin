package io.github.durun.vst3kotlin.samples

fun vstFileSample() {
    val path = Path.of("path/to/plugin.vst3")
    val repository: VstClassRepository = VstFile.of(path)
    val classId = UID("0011223344556677889900AABBCCDDEE")
    val vstClass: VstClass = repository[classId] ?: error("Not found")

    // Use vstClass
}

fun audioInstanceSample(vstClass: VstClass, processData: ProcessData) {
    val instance: AudioInstance = vstClass.createAudioInstance()

    instance.process(processData)   // Use instance

    instance.close()
}

fun processDataSample() {
    val processContext = alloc<ProcessContext>().processContextOf(
        playing = true,
        sampleRate = 48000.0,
        projectTimeSamples = framePos,
        tempo = 120.0,      // optional
        timeSig = 4 over 4  // optional
    )
    val processData = alloc<ProcessData>().processDataOf(
        context = processContext.ptr,
        mode = ProcessMode.Realtime,
        inputAudio = inBuffer,
        outputAudio = outBuffer,
        inputParam = buildParameterChanges {
            put(paramID = 0u, value = 2.0)
        }.placeToCInterface(nativeHeap)
    )

    // Use processData

    nativeHeap.free(processContext)
    nativeHeap.free(processData)
}


fun controllerInstanceSample(vstClass: VstClass) {
    val instance: ControllerInstance = vstClass.createAudioInstance()

    // Use instance

    instance.close()
}