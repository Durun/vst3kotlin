package io.github.durun.path

class WindowsPath
private constructor(
	private val drive: Char?,
	private val elements: List<String>
) : Path {
	companion object {
		val fileSystem: FileSystem = FileSystem.Windows
		fun of(path: String): WindowsPath {
			val prefix = path.substring(0..2).toUpperCase().let {
				it.takeIf { it.matches(rootPattern) }
			}
			val body = prefix?.let {
				path.drop(it.length)
			} ?: path
			return WindowsPath(
				drive = prefix?.first(),
				elements = body.splitToElements()
			)
		}

		private val rootPattern = Regex("[A-Z]:\\\\")
		private fun String.splitToElements() = this.split("\\").filterNot { it.isEmpty() }
	}

	override val isAbsolute: Boolean
		get() = drive != null
	override val name: String
		get() = TODO("Not yet implemented")
	override val nameWithoutExtension: String
		get() = TODO("Not yet implemented")

	override fun resolve(other: String): Path {
		return of("$this\\$other")
	}

	override fun resolve(other: Path): Path {
		return of("$this\\$other")
	}

	override fun toString(): String {
		val prefix = drive?.let { "$it:\\" }.orEmpty()
		return prefix + elements.joinToString("\\")
	}
}