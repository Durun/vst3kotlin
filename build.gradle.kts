allprojects {
    group = "io.github.durun.vst3kotlin"
    version = "0.1"

    repositories {
        mavenCentral()
        jcenter()
    }
}

tasks.withType<Wrapper> {
    gradleVersion = "6.8.2"
    distributionType = Wrapper.DistributionType.ALL
}