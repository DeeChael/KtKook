plugins {
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
    compileOnly(rootProject)
    compileOnly("com.github.SNWCreations:KookBC:1a03b500")
    compileOnly("net.fabricmc:sponge-mixin:0.11.4+mixin.0.8.5")

    testImplementation(platform("org.junit:junit-bom:5.9.1"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.test {
    useJUnitPlatform()
}