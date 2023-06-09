plugins {
    id("com.github.johnrengelman.shadow") version "7.1.2"
    kotlin("jvm") version "1.8.0"
}

group = "net.deechael"
version = "1.00.0"

repositories {
    mavenCentral()
    maven {
        url = uri("https://jitpack.io")
    }
    maven {
        url = uri("https://maven.fabricmc.net/")
    }
}

dependencies {
    compileOnly("com.github.SNWCreations:KookBC:1a03b500")

    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:1.8.21")

    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}

kotlin {
    jvmToolchain(8)
}