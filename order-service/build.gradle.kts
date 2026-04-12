plugins {
    id("org.springframework.boot")
    id("com.google.protobuf")
    id("org.graalvm.buildtools.native")
}

dependencies {
    implementation(project(":common-domain"))

    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    implementation("org.springframework.boot:spring-boot-starter-data-redis")
    implementation("org.springframework.boot:spring-boot-starter-aop")
    runtimeOnly("org.postgresql:postgresql")

    // [Resilience4j]
    implementation("io.github.resilience4j:resilience4j-spring-boot3:2.2.0")

    // [Kafka/Debezium Outbox]
    implementation("com.fasterxml.jackson.core:jackson-databind")

    // [gRPC Client]
    implementation("net.devh:grpc-spring-boot-starter:${project.extra["grpcSpringBootVersion"]}")
    implementation("io.grpc:grpc-stub:${project.extra["grpcVersion"]}")
    implementation("io.grpc:grpc-protobuf:${project.extra["grpcVersion"]}")
    implementation("javax.annotation:javax.annotation-api:1.3.2")

    // [Lombok]
    compileOnly("org.projectlombok:lombok")
    annotationProcessor("org.projectlombok:lombok")
    
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

sourceSets {
    main {
        java {
            srcDir("$buildDir/generated/source/proto/main/java")
            srcDir("$buildDir/generated/source/proto/main/grpc")
        }
    }
}
