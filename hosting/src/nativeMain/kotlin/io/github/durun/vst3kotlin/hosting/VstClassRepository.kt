package io.github.durun.vst3kotlin.hosting

import io.github.durun.vst3kotlin.pluginterface.base.ClassInfo
import io.github.durun.vst3kotlin.pluginterface.base.UID

/**
 * [VstClass]の集約です。
 *
 * @sample io.github.durun.vst3kotlin.samples.vstFileSample
 */
interface VstClassRepository {
    /**
     * 持っている[VstClass]の一覧を[ClassInfo]として返します。
     */
    val classInfos: Collection<ClassInfo>

    /**
     * Class ID から [VstClass]を返します。
     * Class IDは[classInfos]から取得できます。
     * @param classId 取得する[VstClass]のclass ID
     */
    operator fun get(classId: UID): VstClass?
}




