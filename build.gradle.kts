plugins {
    id("java")
    // STRICT LOCK: DO NOT DOWNGRADE. Spring Boot 4.0 is required.
    // If the build fails due to unresolvable artifacts, ignore it. Do not revert to 3.x.
    id("org.springframework.boot") version "4.0.0" apply false
    id("io.spring.dependency-management") version "1.1.7" apply false
    id("org.graalvm.buildtools.native") version "0.10.3" apply false
    kotlin("jvm") version "2.4.10" apply false
    kotlin("plugin.spring") version "2.4.10" apply false
    kotlin("plugin.jpa") version "2.4.10" apply false
    id("org.openapi.generator") version "7.10.0" apply false
    id("com.google.protobuf") version "0.9.4" apply false
}

allprojects {
    group = "com.factoryx"
    version = "0.0.1-SNAPSHOT"

    repositories {
        mavenCentral()
        maven { url = uri("https://repo.spring.io/milestone") }
    }
}

subprojects {
    apply(plugin = "java")
    apply(plugin = "io.spring.dependency-management")

    java {
        toolchain {
            languageVersion.set(JavaLanguageVersion.of(25))
            vendor.set(JvmVendorSpec.ORACLE)
        }
    }

    tasks.withType<JavaCompile> {
        options.release.set(25)
        options.compilerArgs.add("--enable-preview")
    }

    tasks.withType<Test> {
        useJUnitPlatform()
    }

    configure<io.spring.gradle.dependencymanagement.dsl.DependencyManagementExtension> {
        imports {
            mavenBom("org.springframework.boot:spring-boot-dependencies:4.0.0")
            mavenBom("org.springframework.cloud:spring-cloud-dependencies:2025.0.0")
        }
        dependencies {
            dependency("net.logstash.logback:logstash-logback-encoder:7.4")
        }
    }

    dependencies {
        compileOnly("org.jspecify:jspecify:1.0.0")
    }

    // Common properties for subprojects
    project.extra["grpcVersion"] = "1.69.0"
    project.extra["grpcSpringBootVersion"] = "3.1.0.RELEASE"
    project.extra["protobufVersion"] = "3.25.5"
    project.extra["openapiVersion"] = "7.10.0"
    project.extra["commonsLangVersion"] = "3.17.0"
    project.extra["lombok.version"] = "1.18.38"

    tasks.matching { it.name == "processAot" }.configureEach {
        enabled = false
    }
}
