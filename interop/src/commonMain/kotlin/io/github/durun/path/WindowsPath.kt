package io.github.durun.path

import io.github.durun.path.WindowsPath.Companion.checkIsValidPath

class WindowsPath
private constructor(
    private val drive: String?,
    private val elements: List<String>
) : Path {
    companion object {
        val fileSystem: FileSystem = FileSystem.Windows
        fun of(path: String): WindowsPath {
            path.checkIsValidPath()
            val drive = path.replace(absolutePattern, "$1").takeIf { path.matches(absolutePattern) }?.toUpperCase()
            val body = path.replace(absolutePattern, "$2")
            println(drive)
            println(body)
            require(drive != null || path.matches(relativePattern)) { "Invalid Windows path: $path" }
            return WindowsPath(
                drive = drive,
                elements = body.splitToElements()
            )
        }

        private val absolutePattern = Regex("^([a-zA-Z^/\\\\]+):[\\\\/]([^:]*)$") // ${DRIVE}:${BODY}
        private val relativePattern = Regex("^[^\\\\/][^:]*$") // ${BODY}
        private val invalidCharacters = listOf("\n", "\r", "\b")
        private fun String.isAbsolutePath() {
            val dropDrive = this.dropWhile { it != ':' }
            require(dropDrive.isNotEmpty())
            dropDrive.startsWith(":/")
        }

        private fun String.splitToElements() = this.split("\\", "/").filterNot { it.isEmpty() }
        private fun String.checkIsValidPath() {
            require(invalidCharacters.all { !contains(it) }) { "Invalid Windows path: $this" }
            require(!contains("//")) { "Invalid Windows path: $this" }
            require(!contains("\\\\")) { "Invalid Windows path: $this" }
        }
    }

    override val isAbsolute: Boolean
        get() = drive != null
    override val name: String
        get() = elements.lastOrNull().orEmpty()
    override val nameWithoutExtension: String
        get() {
            val nameDot = name.dropLastWhile { it != '.' }
            return if (nameDot.isNotEmpty()) nameDot.dropLast(1)
            else name
        }

    override fun resolve(other: String): Path {
        return resolve(of(other))
    }

    override fun resolve(other: Path): Path {
        val prefix = this.toString().dropLastWhile { it == '\\' }
        return of("$prefix\\$other")
    }

    override fun toString(): String {
        val prefix = drive?.let { "$it:\\" }.orEmpty()
        return prefix + elements.joinToString("\\")
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false

        other as WindowsPath

        if (drive != other.drive) return false
        if (elements != other.elements) return false

        return true
    }

    override fun hashCode(): Int {
        var result = drive?.hashCode() ?: 0
        result = 31 * result + elements.hashCode()
        return result
    }
}