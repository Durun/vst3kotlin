allprojects {
    group = "io.github.durun.vst3kotlin"
    version = "0.1"
}

tasks.withType<Wrapper> {
    gradleVersion = "6.8.2"
    distributionType = Wrapper.DistributionType.ALL
}