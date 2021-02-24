package io.github.durun.path

import platform.posix.F_OK
import platform.posix.access

actual object Files {
    actual fun exists(path: String): Boolean {
        return access(path, F_OK) == 0
    }

    actual fun exists(path: Path): Boolean = exists(path.toString())
}