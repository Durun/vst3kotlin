import de.undercouch.gradle.tasks.download.Download
import de.undercouch.gradle.tasks.download.Verify
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTargetWithHostTests
import org.jetbrains.kotlin.gradle.tasks.CInteropProcess
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("multiplatform") version "1.4.30"
    id("de.undercouch.download") version "4.1.1"
}

repositories {
    mavenCentral()
}

evaluationDependsOn(":vst3cwrapper")
val cwrapper = project(":vst3cwrapper")
dependencies {
    commonMainImplementation(kotlin("stdlib-common"))

    // Test Dependencies
    val kotestVersion = "4.4.0"
    commonTestImplementation(kotlin("test-common"))
    commonTestImplementation(kotlin("test-annotations-common"))
    commonTestImplementation("io.kotest:kotest-assertions-core:$kotestVersion")
    commonTestImplementation("io.kotest:kotest-property:$kotestVersion")
}

val cwrapperDef = file(buildDir.resolve("cinterop/cwrapper.def"))
tasks {
    val cinteropDef by creating {
        doLast {
            cwrapperDef.parentFile.mkdirs()
            if (!cwrapperDef.exists()) cwrapperDef.createNewFile()
            cwrapperDef.writeText(
                """
                staticLibraries.linux_x64 = libvst3cwrapper.a
                staticLibraries.macos_x64 = libvst3cwrapper.a
                staticLibraries.mingw_x64 = vst3cwrapper.lib
                libraryPaths.linux_x64 = ${cwrapper.buildDir.resolve("lib/main/release/linux")}
                libraryPaths.macos_x64 = ${cwrapper.buildDir.resolve("lib/main/release/macos")}
                libraryPaths.mingw_x64 = ${cwrapper.buildDir.resolve("lib/main/release/windows")}
            """.trimIndent()
            )
        }
    }
    withType(CInteropProcess::class) {
        dependsOn(cwrapper.tasks["copyHeaders"])
        dependsOn(cwrapper.tasks["createReleaseLinux"])
        dependsOn(cinteropDef)
    }

    withType(KotlinCompile::class).all {
        kotlinOptions {
            freeCompilerArgs += "-Xopt-in=kotlin.RequiresOptIn"
        }
    }
}

kotlin {
    fun KotlinNativeTargetWithHostTests.configureTarget() {
        compilations.getByName("main") {
            cinterops {
                create("cwrapper") {
                    this.defFile = cwrapperDef
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
            sharedLib()
        }
    }
    linuxX64 { configureTarget() }
    macosX64 { configureTarget() }
    mingwX64("windowsX64") { configureTarget() }
}

// for testing
tasks {
    val zipPath = buildDir.resolve("vst3-samples-linux.zip")
    val downloadSamplesLinux by creating(Download::class) {
        src("https://github.com/Durun/vst3experiment/releases/download/samples/vst3-samples-linux.zip")
        dest(zipPath)
    }
    val verifySamplesLinux by creating(Verify::class) {
        dependsOn(downloadSamplesLinux)
        src(zipPath)
        algorithm("MD5")
        checksum("7e1da79fc15fe53ce06aff13464f5c08")
    }
    val unzipSamplesLinux by creating(Copy::class) {
        dependsOn(verifySamplesLinux)
        from(zipTree(zipPath))
        val destRoot = projectDir.resolve("src/commonTest/resources/vst3")
        into(destRoot)
        eachFile {
            val dest =
                if (2 < relativePath.segments.size) relativePath.segments.drop(2).joinToString("/")
                else relativePath.pathString
            copyTo(destRoot.resolve(dest))
        }
    }
    getByName("linuxX64Test") {
        dependsOn(unzipSamplesLinux)
    }
}