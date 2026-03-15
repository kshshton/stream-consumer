plugins {
    kotlin("jvm") version "2.0.21"
    kotlin("plugin.serialization") version "2.0.21"
    application
}

group = "com.example"
version = "1.0-SNAPSHOT"

kotlin {
    jvmToolchain(21)
}

repositories {
    mavenCentral()
}

val kafkaVersion = "3.9.1"

dependencies {
    implementation("org.apache.kafka:kafka-streams:$kafkaVersion")
    implementation("org.slf4j:slf4j-simple:2.0.13")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.7.3")
    implementation("com.fasterxml.jackson.dataformat:jackson-dataformat-yaml:2.17.1")
    implementation("com.fasterxml.jackson.module:jackson-module-kotlin:2.17.1")

    testImplementation("org.apache.kafka:kafka-streams-test-utils:$kafkaVersion")
    testImplementation(kotlin("test-junit5"))
}

application {
    mainClass.set("com.example.simplepipeline.OneFileSensorServiceKt")
}

tasks.test {
    useJUnitPlatform()
}

tasks.register<JavaExec>("runConfigDrivenService") {
    group = "application"
    description = "Run the config-driven sensor pipeline service."
    classpath = sourceSets["main"].runtimeClasspath
    mainClass.set("com.example.configpipeline.ConfigDrivenSensorServiceKt")
}
