import de.undercouch.gradle.tasks.download.Download
import de.undercouch.gradle.tasks.download.Verify
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget
import org.jetbrains.kotlin.gradle.tasks.CInteropProcess
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile


// other project reference
evaluationDependsOn(":util")
evaluationDependsOn(":window")
evaluationDependsOn(":vst3cwrapper")
val cwrapper = project(":vst3cwrapper")

plugins {
	kotlin("multiplatform") version "1.4.31"
	id("de.undercouch.download") version "4.1.1"
}

repositories {
	mavenCentral()
}

val kotestVersion = "4.4.1"
dependencies {
	// Main Dependencies
	commonMainImplementation(kotlin("stdlib-common"))
	commonMainImplementation(project(":util"))
	commonMainImplementation(project(":window"))

	// Test Dependencies
	commonTestImplementation(kotlin("test-common"))
	commonTestImplementation(kotlin("test-annotations-common"))
	commonTestImplementation("io.kotest:kotest-assertions-core:$kotestVersion")
	commonTestImplementation("io.kotest:kotest-property:$kotestVersion")
}

val cwrapperDef = file(buildDir.resolve("cinterop/cwrapper.def"))
val os = org.gradle.internal.os.OperatingSystem.current()

kotlin {
	// OS setting
	when {
		os.isWindows -> mingwX64("windowsX64")
		os.isMacOsX -> macosX64()
		os.isLinux -> linuxX64()
	}

	// settings for targets
	targets.withType<KotlinNativeTarget>().all {
		sourceSets {
			getByName("${targetName}Main").apply {
				kotlin.srcDir("src/nativeMain/kotlin")
				if (os.isUnix) kotlin.srcDir("src/unixMain/kotlin")
			}
			getByName("${targetName}Test").apply {
				kotlin.srcDir("src/nativeTest/kotlin")
				if (os.isUnix) kotlin.srcDir("src/unixTest/kotlin")
				dependencies {
					implementation(kotlin("test-common"))
					implementation(kotlin("test-annotations-common"))
					implementation("io.kotest:kotest-assertions-core:$kotestVersion")
					implementation("io.kotest:kotest-property:$kotestVersion")
				}
			}
		}
		compilations.getByName("main") {
			cinterops {
				create("cwrapper") {
					defFile = cwrapperDef
					includeDirs.allHeaders(
						cwrapper.projectDir.resolve("src/main/public"),
						cwrapper.buildDir.resolve("headers")
					)
					headers(fileTree(cwrapper.projectDir.resolve("src/main/public")))
				}
			}
			kotlinOptions {
				freeCompilerArgs += "-Xopt-in=kotlin.RequiresOptIn"
			}
		}
		binaries {
			staticLib()
		}
	}
}

tasks { // for compilation
	val cinteropDef by creating {
		fun File.unixPath() = when {
			os.isWindows -> this.toURI().toString().drop("file:/".length)
			else -> this.path
		}
		doLast {
			cwrapperDef.parentFile.mkdirs()
			if (!cwrapperDef.exists()) cwrapperDef.createNewFile()
			val unixStyleText = """
                staticLibraries.linux_x64 = libvst3cwrapper.a
                staticLibraries.macos_x64 = libvst3cwrapper.a
                staticLibraries.mingw_x64 = vst3cwrapper.lib
                libraryPaths.linux_x64 = ${cwrapper.buildDir.resolve("lib/main/release/linux").unixPath()}
                libraryPaths.macos_x64 = ${cwrapper.buildDir.resolve("lib/main/release/macos").unixPath()}
                libraryPaths.mingw_x64 = ${cwrapper.buildDir.resolve("lib/main/release/windows").unixPath()}
            """.trimIndent()
			val text = when {
				os.isWindows -> unixStyleText.replace("\n", "\r\n")
				else -> unixStyleText
			}
			cwrapperDef.writeText(text)
		}
	}
	withType(CInteropProcess::class) {
		dependsOn(cwrapper.tasks["assemble"])
		dependsOn(cinteropDef)
	}
	withType(KotlinCompile::class).all {
		kotlinOptions {
			freeCompilerArgs += "-Xopt-in=kotlin.RequiresOptIn"
		}
	}
}

tasks { // for testing
	// OS setting
	val targetName = when {
		os.isWindows -> "windowsX64"
		os.isMacOsX -> "macosX64"
		os.isLinux -> "linuxX64"
		else -> throw GradleException("${os.familyName} is not supported.")
	}
	val downloadAll by creating {}
	val vst3sdkSamples by when {
		os.isWindows -> creatingDownloadZip(
			url = "https://github.com/Durun/vst3experiment/releases/download/samples/vst3samples-windowsX64.zip",
			md5 = "0f2d5aa9999f2fd40270bafa459502c4",
			dest = projectDir.resolve("src/windowsX64Test/resources/vst3"),
			fromDepth = 2
		)
		os.isMacOsX -> creatingDownloadZip(
			url = "https://github.com/Durun/vst3experiment/releases/download/samples/vst3samples-macosX64.zip",
			md5 = "b143d6fd79cbf345212f08a9d0659d57",
			dest = projectDir.resolve("src/macosX64Test/resources/vst3"),
			fromDepth = 2
		)
		os.isLinux -> creatingDownloadZip(
			url = "https://github.com/Durun/vst3experiment/releases/download/samples/vst3samples-linuxX64.zip",
			md5 = "7762726a6da2d2b2bd34ac54c7184dda",
			dest = projectDir.resolve("src/linuxX64Test/resources/vst3"),
			fromDepth = 2
		)
		else -> throw GradleException("${os.familyName} is not supported.")
	}
	getByName("${targetName}Test") {
		dependsOn(vst3sdkSamples)
	}
}

fun TaskContainerScope.creatingDownloadZip(url: String, md5: String, dest: File, fromDepth: Int = 0) = creating {
	val download = create<Download>("${name}Download") {
		src(url)
		dest(temporaryDir)
	}
	val downloadAll by getting
	downloadAll.dependsOn(download)
	val outputFile = download.outputFiles.single()
	val verify = create<Verify>("${name}Verify") {
		dependsOn(download)
		src(outputFile)
		algorithm("MD5")
		checksum(md5)
	}
	val unzip = create<Copy>("${name}Unzip") {
		dependsOn(verify)
		from(zipTree(outputFile))
		val destRoot = projectDir.resolve(dest)
		into(destRoot)
		eachFile {
			val dest =
				if (fromDepth < relativePath.segments.size) relativePath.segments.drop(fromDepth).joinToString("/")
				else relativePath.pathString
			copyTo(destRoot.resolve(dest))
		}
	}
	dependsOn(unzip)
}
