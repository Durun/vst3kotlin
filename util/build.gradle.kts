import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget


plugins {
    kotlin("multiplatform") version "1.4.31"
}

val kotestVersion = "4.4.1"
dependencies {
    // Main Dependencies
    commonMainImplementation(kotlin("stdlib-common"))

    // Test Dependencies
    commonTestImplementation(kotlin("test-common"))
    commonTestImplementation(kotlin("test-annotations-common"))
    commonTestImplementation("io.kotest:kotest-assertions-core:$kotestVersion")
    commonTestImplementation("io.kotest:kotest-property:$kotestVersion")
}

kotlin {
    // OS setting
    val os = org.gradle.internal.os.OperatingSystem.current()
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
        binaries {
            staticLib()
        }
    }
}

