import org.ajoberstar.grgit.Grgit

// OS setting
val os = org.gradle.internal.os.OperatingSystem.current()
val targetName = when {
    os.isWindows -> "Windows"
    os.isMacOsX -> "Macos"
    os.isLinux -> "Linux"
    else -> throw GradleException("${os.familyName} is not supported.")
}

plugins {
    id("org.ajoberstar.grgit") version "4.1.0"
    `cpp-library`
    `cpp-unit-test`
    `maven-publish`
}

library {
    linkage.set(listOf(Linkage.STATIC))
    targetMachines.add(machines.linux.x86_64)
    targetMachines.add(machines.macOS.x86_64)
    targetMachines.add(machines.windows.x86_64)

    source {
        from(file("src/main/cpp"))
        when {
            os.isWindows -> from(file("src/windowsMain/cpp"))
            os.isUnix -> from(file("src/unixMain/cpp"))
        }
    }
}

val sdkDir = buildDir.resolve("sdk")
tasks {
    val checkoutSdk by creating {
        doLast {
            checkout(
                url = "https://github.com/steinbergmedia/vst3sdk.git",
                branch = "v3.7.1_build_50",
                dir = sdkDir
            )
        }
    }

    val copyHeaders by creating(Copy::class) {
        dependsOn(checkoutSdk)
        from(sdkDir)
        into(buildDir.resolve("headers/"))
        include("**/*.h")
        includeEmptyDirs = false
    }

    withType<CppCompile> {
        dependsOn(checkoutSdk)
        includes(sdkDir)
        when {
            os.isWindows -> compilerArgs.add("/GS-")
        }
    }

    val assemble by getting {
        dependsOn(copyHeaders)
        dependsOn("createRelease$targetName")
    }
}

// Utils
fun checkout(url: Any, branch: String, dir: Any) {
    val repository = runCatching {
        Grgit.open { this.dir = dir }
    }.getOrElse {
        mkdir(file(dir).parentFile)
        exec {
            workingDir = file(dir).parentFile
            commandLine = listOf("git", "clone", "--recursive", "$url", file(dir).name)
            standardOutput = System.out
        }.assertNormalExitValue()
        Grgit.open { this.dir = dir }
    } ?: throw kotlin.IllegalStateException("Cannot clone $url")
    repository.let {
        it.checkout { this.branch = branch }
    }
}