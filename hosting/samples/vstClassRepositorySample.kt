package io.github.durun.vst3kotlin.samples

fun vstFileSample() {
	val path = Path.of("path/to/plugin.vst3")
	val repository: VstClassRepository = VstFile.of(path)
	val classId = UID("0011223344556677889900AABBCCDDEE")
	val vstClass: VstClass = repository[classId] ?: error("Not found")

	// Use vstClass
}
