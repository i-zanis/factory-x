plugins {
    id("org.springframework.boot")
    id("org.openapi.generator")
    id("com.google.protobuf")
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
    implementation("net.devh:grpc-spring-boot-starter:${project.extra["grpcSpringBootVersion"]}")
    implementation("io.grpc:grpc-stub:${project.extra["grpcVersion"]}")
    implementation("io.grpc:grpc-protobuf:${project.extra["grpcVersion"]}")

    // Java Annotations (needed for gRPC generated code on Java 9+)
    implementation("javax.annotation:javax.annotation-api:1.3.2")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
}

protobuf {
    protoc {
        artifact = "com.google.protobuf:protoc:${project.extra["protobufVersion"]}"
    }
    plugins {
        create("grpc") {
            artifact = "io.grpc:protoc-gen-grpc-java:${project.extra["grpcVersion"]}"
        }
    }
    generateProtoTasks {
        all().forEach {
            it.plugins {
                create("grpc")
            }
        }
    }
}

openApiGenerate {
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
