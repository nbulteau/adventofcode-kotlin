plugins {
    id("com.github.ben-manes.versions") version "0.53.0"

    kotlin("jvm") version "2.2.21"
    application
}

group = "me.nicolas.adventofcode"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("tools.aqua:z3-turnkey:4.14.1") // use Z3 to solve 2023 day24 part two
    implementation("net.sf.jung:jung-algorithms:2.1.1") // use JUNG to solve 2023 day25
    implementation("net.sf.jung:jung-graph-impl:2.1.1")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.9.0") // to solve 2015 day 12
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.10.1") // use coroutines for parallel processing

    testImplementation(kotlin("test-junit5"))
    testImplementation("org.assertj:assertj-core:3.27.6")
}

tasks.withType<Test>().configureEach {
    maxParallelForks = (Runtime.getRuntime().availableProcessors() / 2).coerceAtLeast(1)
}

tasks.test {
    useJUnitPlatform()
    testLogging {
        showStandardStreams = true
        events("passed", "skipped", "failed")
    }
}

