package io.github.durun.path

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import kotlin.test.Test


class WindowsPathTest {
	@Test
	fun of() {
		shouldThrow<Exception> { WindowsPath.of("double//slash").let { println(it) } }
		shouldThrow<Exception> { WindowsPath.of("invalid/\nnewline").let { println(it) } }
		shouldThrow<Exception> { WindowsPath.of("invalid/\rreturn").let { println(it) } }
		shouldThrow<Exception> { WindowsPath.of("invalid/\b").let { println(it) } }
		shouldThrow<Exception> { WindowsPath.of("/no/drive").let { println(it) } }
	}

	@Test
	fun isAbsolute() {
		WindowsPath.of("C:/").isAbsolute shouldBe true
		WindowsPath.of("C:/home/user").isRelative shouldBe false
		WindowsPath.of(".").isRelative shouldBe true
		WindowsPath.of("../folder/").isAbsolute shouldBe false
	}

	@Test
	fun name() {
		WindowsPath.of("home/a.out").name shouldBe "a.out"
		WindowsPath.of("home/.git").name shouldBe ".git"
		WindowsPath.of("C:/a.out.old").name shouldBe "a.out.old"
		WindowsPath.of("C:/.git").name shouldBe ".git"
	}

	@Test
	fun nameWithoutExtension() {
		WindowsPath.of("home/a.out").nameWithoutExtension shouldBe "a"
		WindowsPath.of("home/a.out.old").nameWithoutExtension shouldBe "a.out"
		WindowsPath.of("home/.git").nameWithoutExtension shouldBe ""
		WindowsPath.of("C:/a.out.old").nameWithoutExtension shouldBe "a.out"
		WindowsPath.of("C:/.git").nameWithoutExtension shouldBe ""
	}

	@Test
	fun resolve_String() {
		WindowsPath.of("c:/").resolve("home/user") shouldBe WindowsPath.of("C:/home/user")
		WindowsPath.of("user").resolve("bin") shouldBe WindowsPath.of("user/bin")
		shouldThrow<Exception> { WindowsPath.of("C:/").resolve("D:/absolute/path") }
		shouldThrow<Exception> { WindowsPath.of("C:/").resolve("path/with/\n/invalid/char") }
		shouldThrow<Exception> { WindowsPath.of("C:/").resolve("path/with/double//slash/") }
	}

	@Test
	fun resolve_Path() {
		WindowsPath.of("D:/").resolve(WindowsPath.of("home/user")) shouldBe WindowsPath.of("D:/home/user")
		WindowsPath.of("user").resolve(WindowsPath.of("bin")) shouldBe WindowsPath.of("user/bin")
		shouldThrow<Exception> { WindowsPath.of("C:/").resolve(WindowsPath.of("/absolute/path")) }
	}

	@Test
	fun testToString() {
		WindowsPath.of("C:/").toString() shouldBe "C:\\"
		WindowsPath.of("C:/home/user/").toString() shouldBe "C:\\home\\user"
		WindowsPath.of(".").toString() shouldBe "."
		WindowsPath.of("../folder/").toString() shouldBe "..\\folder"
	}

	@Test
	fun testEquals() {
		WindowsPath.of("C:/home/user/") shouldBe WindowsPath.of("C:/home/user")
		WindowsPath.of("home/user/") shouldNotBe WindowsPath.of("C:/home/user/")
	}
}