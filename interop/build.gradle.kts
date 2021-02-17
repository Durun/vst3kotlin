import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTargetWithHostTests
import org.jetbrains.kotlin.gradle.tasks.CInteropProcess
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("multiplatform") version "1.4.30"
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