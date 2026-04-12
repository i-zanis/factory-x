plugins {
    id("java")
    id("org.springframework.boot") version "3.4.0" apply false
    id("io.spring.dependency-management") version "1.1.7" apply false
    kotlin("jvm") version "2.0.21" apply false
    kotlin("plugin.spring") version "2.0.21" apply false
    kotlin("plugin.jpa") version "2.0.21" apply false
    id("org.openapi.generator") version "7.10.0" apply false
    id("com.google.protobuf") version "0.9.4" apply false
}

allprojects {
    group = "com.factoryx"
    version = "0.0.1-SNAPSHOT"

    repositories {
        mavenCentral()
    }
}

subprojects {
    apply(plugin = "java")
    apply(plugin = "io.spring.dependency-management")

    java {
        toolchain {
            languageVersion.set(JavaLanguageVersion.of(25))
        }
    }

    configure<io.spring.gradle.dependencymanagement.dsl.DependencyManagementExtension> {
        imports {
            mavenBom("org.springframework.boot:spring-boot-dependencies:3.4.0")
            mavenBom("org.springframework.cloud:spring-cloud-dependencies:2024.0.0")
        }
    }

    // Common properties for subprojects
    project.extra["grpcVersion"] = "1.69.0"
    project.extra["grpcSpringBootVersion"] = "3.1.0"
    project.extra["protobufVersion"] = "3.25.5"
    project.extra["openapiVersion"] = "7.10.0"
    project.extra["commonsLangVersion"] = "3.17.0"
}
