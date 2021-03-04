package io.github.durun.io

import io.kotest.assertions.throwables.shouldThrow
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import kotlin.test.Test


class UnixPathTest {
	@Test
	fun of() {
		shouldThrow<Exception> { UnixPath.of("double//slash") }
		shouldThrow<Exception> { UnixPath.of("invalid/\nnewline") }
		shouldThrow<Exception> { UnixPath.of("invalid/\rreturn") }
		shouldThrow<Exception> { UnixPath.of("invalid/\b") }
	}

	@Test
	fun isAbsolute() {
		UnixPath.of("/").isAbsolute shouldBe true
		UnixPath.of("/home/user").isRelative shouldBe false
		UnixPath.of(".").isRelative shouldBe true
		UnixPath.of("../folder/").isAbsolute shouldBe false
	}

	@Test
	fun name() {
		UnixPath.of("home/a.out").name shouldBe "a.out"
		UnixPath.of("home/.git").name shouldBe ".git"
		UnixPath.of("/a.out.old").name shouldBe "a.out.old"
		UnixPath.of("/.git").name shouldBe ".git"
	}

	@Test
	fun nameWithoutExtension() {
		UnixPath.of("home/a.out").nameWithoutExtension shouldBe "a"
		UnixPath.of("home/a.out.old").nameWithoutExtension shouldBe "a.out"
		UnixPath.of("home/.git").nameWithoutExtension shouldBe ""
		UnixPath.of("/a.out.old").nameWithoutExtension shouldBe "a.out"
		UnixPath.of("/.git").nameWithoutExtension shouldBe ""
	}

	@Test
	fun resolve_String() {
		UnixPath.of("/").resolve("home/user") shouldBe UnixPath.of("/home/user")
		UnixPath.of("user").resolve("bin") shouldBe UnixPath.of("user/bin")
		shouldThrow<Exception> { UnixPath.of("/").resolve("/absolute/path") }
		shouldThrow<Exception> { UnixPath.of("/").resolve("path/with/\n/invalid/char") }
		shouldThrow<Exception> { UnixPath.of("/").resolve("path/with/double//slash/") }
	}

	@Test
	fun resolve_Path() {
		UnixPath.of("/").resolve(UnixPath.of("home/user")) shouldBe UnixPath.of("/home/user")
		UnixPath.of("user").resolve(UnixPath.of("bin")) shouldBe UnixPath.of("user/bin")
		shouldThrow<Exception> { UnixPath.of("/").resolve(UnixPath.of("/absolute/path")) }
	}

	@Test
	fun testToString() {
		UnixPath.of("/").toString() shouldBe "/"
		UnixPath.of("/home/user/").toString() shouldBe "/home/user"
		UnixPath.of(".").toString() shouldBe "."
		UnixPath.of("../folder/").toString() shouldBe "../folder"
	}

	@Test
	fun testEquals() {
		UnixPath.of("/home/user/") shouldBe UnixPath.of("/home/user")
		UnixPath.of("home/user/") shouldNotBe UnixPath.of("/home/user/")
	}
}