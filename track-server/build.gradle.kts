import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    kotlin("jvm") version "1.3.50"
}
dependencies {
    implementation("org.springframework.boot:spring-boot-starter-webflux:2.1.7.RELEASE")
    implementation("org.springframework.boot:spring-boot-starter-data-mongodb-reactive:2.1.7.RELEASE")
    implementation(kotlin("stdlib-jdk8"))
}
repositories {
    mavenCentral()
}
val compileKotlin: KotlinCompile by tasks
compileKotlin.kotlinOptions {
    jvmTarget = "1.8"
}
val compileTestKotlin: KotlinCompile by tasks
compileTestKotlin.kotlinOptions {
    jvmTarget = "1.8"
}