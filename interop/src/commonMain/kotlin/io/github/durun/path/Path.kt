package io.github.durun.path

interface Path {
    fun resolve(other: String): Path
    fun resolve(other: Path): Path
    override fun toString(): String
    val isAbsolute: Boolean
    val isRelative: Boolean
        get() = !isAbsolute
    val name: String
    val nameWithoutExtension: String

    companion object {
        fun of(path: String): Path = when (DEFAULT_FILESYSTEM) {
            FileSystem.Unix -> UnixPath.of(path)
            FileSystem.Windows -> WindowsPath.of(path)
        }
    }
}

fun Path.exists(): Boolean = Files.exists(this)