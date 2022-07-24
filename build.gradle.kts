import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.7.10"
}

group = "site.caikun.poem"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("com.squareup.okhttp3:okhttp:5.0.0-alpha.10")
    implementation("org.jsoup:jsoup:1.15.2")
    implementation("com.google.code.gson:gson:2.9.0")
    implementation("mysql:mysql-connector-java:8.0.29")
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}

tasks.withType<KotlinCompile> {
    kotlinOptions.jvmTarget = "1.8"
}