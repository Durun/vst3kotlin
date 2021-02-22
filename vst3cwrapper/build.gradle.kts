import org.ajoberstar.grgit.Grgit

// OS setting
val os = org.gradle.internal.os.OperatingSystem.current()
val targetName = when {
    os.isWindows -> "Windows"
    os.isMacOsX -> "Macos"
    os.isLinux -> "Linux"
    else -> throw GradleException("${os.familyName} is not supported.")
}

subprojects {
    apply(plugin = "cpp-library")
    library { linkage.set(listOf(Linkage.STATIC)) }
}
val windowsUtil = project("windowsUtil") {
    library { targetMachines.add(machines.windows.x86_64) }
}
val unixUtil = project("unixUtil") {
    library {
        targetMachines.add(
            when {
                os.isMacOsX -> machines.macOS.x86_64
                else -> machines.linux.x86_64
            }
        )
    }
}


plugins {
    id("org.ajoberstar.grgit") version "4.1.0"
    `cpp-library`
    `cpp-unit-test`
    `maven-publish`
}

repositories {
    mavenLocal()
}

dependencies {
    //implementation("io.github.durun.vst3sdk-cpp:pluginterfaces:3.7.1")
}

library {
    linkage.set(listOf(Linkage.STATIC))
    targetMachines.add(machines.linux.x86_64)
    targetMachines.add(machines.macOS.x86_64)
    targetMachines.add(machines.windows.x86_64)
    binaries.configureEach {
        val osFamily = targetMachine.operatingSystemFamily
        dependencies {
            when {
                osFamily.isWindows -> implementation(windowsUtil)
                else -> implementation(unixUtil)
            }
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
    }
    withType<LinkExecutable> {
        //linkerArgs.add("-ldl")
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