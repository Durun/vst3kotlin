package io.github.durun.path

class UnixPath
private constructor(
	override val isAbsolute: Boolean,
	private val elements: List<String>
) : Path {
	companion object {
		val fileSystem: FileSystem = FileSystem.Unix
		fun of(path: String): UnixPath {
			require(path.isValidPath()) { "invalid path: $path" }
			return UnixPath(
				isAbsolute = path.isAbsolutePath(),
				elements = path.splitToElements()
			)
		}

		private val invalidCharacters = listOf("\n", "\r", "\b")
		private fun String.isAbsolutePath() = this.startsWith("/")
		private fun String.splitToElements() = this.split("/").filterNot { it.isEmpty() }
		private fun String.isValidPath() = invalidCharacters.all { !this.contains(it) } && !this.contains("//")
	}

	override val name: String
		get() = elements.lastOrNull().orEmpty()
	override val nameWithoutExtension: String
		get() {
			val nameDot = name.dropLastWhile { it != '.' }
			return if (nameDot.isNotEmpty()) nameDot.dropLast(1)
			else name
		}

	override fun resolve(other: String): Path {
		return when {
			isRoot() -> of("/$other")
			else -> of("$this/$other")
		}
	}

	override fun resolve(other: Path): Path {
		return when {
			isRoot() -> of("/$other")
			else -> of("$this/$other")
		}
	}

	override fun toString(): String {
		return (if (isAbsolute) "/" else "") + elements.joinToString("/")
	}

	override fun equals(other: Any?): Boolean {
		if (this === other) return true
		if (other == null || this::class != other::class) return false

		other as UnixPath

		if (isAbsolute != other.isAbsolute) return false
		if (elements != other.elements) return false

		return true
	}

	override fun hashCode(): Int {
		var result = isAbsolute.hashCode()
		result = 31 * result + elements.hashCode()
		return result
	}

	private fun isRoot() = elements.isEmpty()
}