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