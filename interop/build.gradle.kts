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

kotlin {
    linuxX64 {
        compilations.getByName("main") {
            cinterops {
                create("cwrapper") {
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
}
tasks.findByName("cinteropCwrapperLinuxX64")?.apply {
    dependsOn(cwrapper.tasks["copyHeaders"])
    dependsOn(cwrapper.tasks["createReleaseLinux"])
}