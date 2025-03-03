import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar

plugins {
    kotlin("jvm") version "2.0.21"
    id("com.gradleup.shadow") version "8.3.0"
    id("application")
}

group = "com.unityrs"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("org.jetbrains.kotlin:kotlin-scripting-common")
    implementation("org.jetbrains.kotlin:kotlin-scripting-jvm")
    implementation("org.jetbrains.kotlin:kotlin-scripting-jvm-host")
    implementation("org.jetbrains.kotlin:kotlin-main-kts")
    implementation("org.jetbrains.kotlin:kotlin-script-runtime")

    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.8.0")

    implementation("io.insert-koin:koin-core:4.0.1")
    implementation("com.google.guava:guava:33.0.0-jre")
    implementation("com.displee:rs-cache-library:7.1.8")
    implementation("org.yaml:snakeyaml:2.0")
    implementation("mysql:mysql-connector-java:8.0.26")
    implementation("io.netty:netty:3.10.6.Final")
    implementation("org.apache.commons:commons-lang3:3.10")
    implementation("com.google.guava:guava:27.0.1-jre")
    implementation("com.google.code.gson:gson:2.8.5")
    implementation("org.slf4j:slf4j-api:1.7.30")
    implementation("com.ea.agentloader:ea-agent-loader:1.0.3")
    implementation("org.w3c:dom:2.3.0-jaxb-1.0.6")
    implementation("org.db4j:kilim:2.0.2")


    annotationProcessor("org.projectlombok:lombok:1.18.36")
    compileOnly("org.projectlombok:lombok:1.18.36")
}

tasks.test {
    useJUnitPlatform()
}
kotlin {
    jvmToolchain(21)
}

application {
    mainClass = "com.hyze.Engine"
}

tasks.withType<ShadowJar> {
    archiveFileName = "unityscape-server.jar"
}