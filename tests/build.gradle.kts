import Build_gradle.Versions.cucumber
import Build_gradle.Versions.testContainers
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    id("org.springframework.boot") version "2.7.5"
    id("io.spring.dependency-management") version "1.0.15.RELEASE"
    kotlin("jvm") version "1.6.21"
    kotlin("plugin.spring") version "1.6.21"
}

group = "org.rrjg.reactive.example"
version = "0.0.1-SNAPSHOT"
java.sourceCompatibility = JavaVersion.VERSION_17

repositories {
    mavenCentral()
}

object Versions {
    const val cucumber = "7.9.0"
    const val testContainers = "1.17.4"
}


dependencies {

    testRuntimeOnly("org.junit.vintage:junit-vintage-engine")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
    testImplementation("io.projectreactor:reactor-test")
    testImplementation("org.springframework.kafka:spring-kafka-test")
    testImplementation("org.testcontainers:junit-jupiter:$testContainers")
    testImplementation("org.testcontainers:kafka:$testContainers")
    testImplementation("org.testcontainers:mongodb:$testContainers")
    testImplementation("org.testcontainers:r2dbc:$testContainers")
    testImplementation("io.cucumber:cucumber-java:$cucumber")
    testImplementation("io.cucumber:cucumber-junit:$cucumber")
    testImplementation("io.cucumber:cucumber-junit-platform-engine:$cucumber")
    testImplementation("io.cucumber:cucumber-spring:$cucumber")
    testImplementation(project(":service-purchase"))
    testImplementation(project(":service-stock"))

}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs = listOf("-Xjsr305=strict")
        jvmTarget = "17"
    }
}
tasks.bootRun {
    systemProperties = System.getProperties() as Map<String, Any>
}
