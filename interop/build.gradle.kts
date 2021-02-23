import de.undercouch.gradle.tasks.download.Download
import de.undercouch.gradle.tasks.download.Verify
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget
import org.jetbrains.kotlin.gradle.tasks.CInteropProcess
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile


// other project reference
evaluationDependsOn(":vst3cwrapper")
val cwrapper = project(":vst3cwrapper")


plugins {
    kotlin("multiplatform") version "1.4.30"
    id("de.undercouch.download") version "4.1.1"
}

repositories {
    mavenCentral()
}

dependencies {
    // Main Dependencies
    commonMainImplementation(kotlin("stdlib-common"))

    // Test Dependencies
    val kotestVersion = "4.4.0"
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
            }
            getByName("${targetName}Test").apply {
                kotlin.srcDir("src/nativeTest/kotlin")
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
            this.isAbsolute -> this.toURI().toString().drop("file:".length)
            else -> this.toURI().toString().drop("file:/".length)
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
    val zipName = "vst3samples-$targetName.zip"
    val zipPath = buildDir.resolve(zipName)
    val downloadSamples by creating(Download::class) {
        src("https://github.com/Durun/vst3experiment/releases/download/samples/$zipName")
        dest(zipPath)
    }
    val verifySamples by creating(Verify::class) {
        dependsOn(downloadSamples)
        src(zipPath)
        algorithm("MD5")
        when (zipName) {
            "vst3samples-linuxX64.zip" -> checksum("7762726a6da2d2b2bd34ac54c7184dda")
            "vst3samples-windowsX64.zip" -> checksum("0f2d5aa9999f2fd40270bafa459502c4")
        }
    }
    val unzipSamples by creating(Copy::class) {
        dependsOn(verifySamples)
        from(zipTree(zipPath))
        val destRoot = projectDir.resolve("src/${targetName}Test/resources/vst3")
        into(destRoot)
        eachFile {
            val dest =
                if (2 < relativePath.segments.size) relativePath.segments.drop(2).joinToString("/")
                else relativePath.pathString
            copyTo(destRoot.resolve(dest))
        }
    }
    getByName("${targetName}Test") {
        dependsOn(unzipSamples)
    }
}