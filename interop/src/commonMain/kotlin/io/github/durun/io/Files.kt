package io.github.durun.io

expect object Files {
    fun exists(path: String): Boolean
    fun exists(path: Path): Boolean
}