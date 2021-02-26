package io.github.durun.path

expect object Files {
    fun exists(path: String): Boolean
    fun exists(path: Path): Boolean
}