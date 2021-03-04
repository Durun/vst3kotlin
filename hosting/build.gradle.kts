import de.undercouch.gradle.tasks.download.Download
import de.undercouch.gradle.tasks.download.Verify
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile


// other project reference
evaluationDependsOn(":util")
evaluationDependsOn(":window")
evaluationDependsOn(":vst3pluginterface")
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
    commonMainImplementation(project(":vst3pluginterface"))

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
    }
}

tasks { // for compilation
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
    val talNoizeMaker by when {
        os.isWindows -> creatingDownloadZip(
            url = "https://tal-software.com//downloads/plugins/install_tal-noisemaker.zip",
            md5 = "5049f5a57cdf06c872c925b6b449e76b",
            dest = projectDir.resolve("src/windowsX64Test/resources/vst3"),
            fromDepth = 0
        )
        os.isMacOsX -> creatingDownloadPkg(
            url = "https://tal-software.com//downloads/plugins/tal-noisemaker-installer.pkg",
            md5 = "6934b18acb462c73a41582d717ec1ea9",
            dest = projectDir.resolve("src/macosX64Test/resources/vst3"),
            fromDepth = 2
        )
        os.isLinux -> creatingDownloadZip(
            url = "https://tal-software.com/downloads/plugins/TAL-NoiseMaker_64_linux.zip",
            md5 = "e6a1a740629123437ff18da93058ea21",
            dest = projectDir.resolve("src/linuxX64Test/resources/vst3"),
            fromDepth = 2
        )
        else -> throw GradleException("${os.familyName} is not supported.")
    }

    val talFilter2 by when {
        os.isWindows -> creatingDownloadZip(
            url = "https://tal-software.com//downloads/plugins/install_TAL-Filter-2.zip",
            md5 = "c4d796ac9391a134ba23f827290d4d9c",
            dest = projectDir.resolve("src/windowsX64Test/resources/vst3"),
            fromDepth = 0
        )
        os.isMacOsX -> creatingDownloadPkg(
            url = "https://tal-software.com//downloads/plugins/TAL-Filter-2-installer.pkg",
            md5 = "bcb8c0d587ac75de0b43d0302b564ad4",
            dest = projectDir.resolve("src/macosX64Test/resources/vst3"),
            fromDepth = 2
        )
        os.isLinux -> creatingDownloadZip(
            url = "https://tal-software.com/downloads/plugins/TAL-Filter-2_64_linux.zip",
            md5 = "175a62684e753eba0a51ec90d00e1f4c",
            dest = projectDir.resolve("src/linuxX64Test/resources/vst3"),
            fromDepth = 2
        )
        else -> throw GradleException("${os.familyName} is not supported.")
    }
    getByName("${targetName}Test") {
        dependsOn(vst3sdkSamples)
        dependsOn(talNoizeMaker)
        dependsOn(talFilter2)
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

fun TaskContainerScope.creatingDownloadPkg(url: String, md5: String, dest: File, fromDepth: Int = 0) = creating {
    val download = create<Download>("${name}Download") {
        src(url)
        dest(temporaryDir)
    }
    val downloadAll by getting
    downloadAll.dependsOn(download)
    val outputFile = download.outputFiles.single()
    val verify = create<Verify>("${name}Verify") {
        //dependsOn(download)
        src(outputFile)
        algorithm("MD5")
        checksum(md5)
    }

    val extract1 = create<Exec>("${name}Extract1") {
        dependsOn(verify)
        workingDir = temporaryDir
        commandLine("xar", "-xf", outputFile)
        isIgnoreExitValue = true
    }

    val extract2 = create("${name}Extract2") {
        dependsOn(extract1)
        doLast {
            val payloads = fileTree(extract1.temporaryDir).asFileTree.filter {
                it.isDirectory || it.name == "Payload"
            }
            payloads.files.forEach {
                println(it)
                exec { commandLine("mv", "$it", "$it.gz") }
                exec { commandLine("gunzip", "$it.gz") }
                exec {
                    workingDir = it.parentFile
                    commandLine("cpio", "-i", "--file=Payload")
                }
            }
        }
    }
    val copy = create<Copy>("${name}Copy") {
        dependsOn(extract2)
        from(fileTree(extract1.temporaryDir))
        include("**/*.vst3/**")
        val destRoot = projectDir.resolve(dest)
        into(destRoot)
        includeEmptyDirs = false
        eachFile {
            val dest =
                if (fromDepth < relativePath.segments.size) relativePath.segments.drop(fromDepth).joinToString("/")
                else relativePath.pathString
            copyTo(destRoot.resolve(dest))
        }
    }
    dependsOn(copy)
}