plugins {
    kotlin("jvm") version "1.9.20"
    application
}

group = "me.nicolas.adventofcode"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    testImplementation(kotlin("test-junit5"))
}

tasks.test {
    useJUnitPlatform()
}

