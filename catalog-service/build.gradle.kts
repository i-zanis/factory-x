plugins {
    id("java")
    id("org.springframework.boot")
    id("org.openapi.generator") version "7.10.0"
    id("com.google.protobuf") version "0.9.4"
}

dependencies {
    implementation(project(":common-domain"))

    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.7.0")

    // DB
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    runtimeOnly("org.postgresql:postgresql")

    // Lombok
    compileOnly("org.projectlombok:lombok")
    annotationProcessor("org.projectlombok:lombok")

    // Swagger UI / OpenAPI Annotations
    implementation("io.swagger.core.v3:swagger-annotations:2.2.27")

    // Jackson
    implementation("org.openapitools:jackson-databind-nullable:0.2.6")

    // gRPC Server & Protobufs
    implementation("net.devh:grpc-spring-boot-starter:3.1.0")
    implementation("io.grpc:grpc-stub:1.69.0")
    implementation("io.grpc:grpc-protobuf:1.69.0")

    // Java Annotations (needed for gRPC generated code on Java 9+)
    implementation("javax.annotation:javax.annotation-api:1.3.2")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
}

protobuf {
    protoc {
        artifact = "com.google.protobuf:protoc:3.25.5"
    }
    plugins {
        id("grpc") {
            artifact = "io.grpc:protoc-gen-grpc-java:1.69.0"
        }
    }
    generateProtoTasks {
        all().forEach {
            it.plugins {
                id("grpc")
            }
        }
    }
}

val openApiGenerate by tasks.registering(org.openapitools.generator.gradle.plugin.tasks.GenerateTask::class) {
    generatorName.set("spring")
    inputSpec.set("$projectDir/src/main/resources/api/catalog-api.yaml")
    outputDir.set("$buildDir/generated/openapi/")
    apiPackage.set("com.factoryx.catalog.api")
    modelPackage.set("com.factoryx.catalog.model")
    configOptions.set(
        mapOf(
            "interfaceOnly" to "true",
            "useSpringBoot3" to "true",
            "useJakartaEe" to "true"
        )
    )
}

sourceSets {
    main {
        java {
            srcDir("$buildDir/generated/openapi/src/main/java")
            srcDir("$buildDir/generated/source/proto/main/java")
            srcDir("$buildDir/generated/source/proto/main/grpc")
        }
    }
}

tasks.named("compileJava") {
    dependsOn("openApiGenerate")
}
