plugins {
    id("java")
    id("org.springframework.boot")
    id("com.google.protobuf") version "0.9.4"
}

dependencies {
    implementation(project(":common-domain"))
    implementation(project(":catalog-service"))

    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springframework.boot:spring-boot-starter-data-jpa")
    runtimeOnly("org.postgresql:postgresql")

    // [Kafka/Debezium Outbox]
    implementation("com.fasterxml.jackson.core:jackson-databind")

    // [gRPC Client]
    implementation("net.devh:grpc-spring-boot-starter:3.1.0")
    implementation("io.grpc:grpc-stub:1.69.0")
    implementation("io.grpc:grpc-protobuf:1.69.0")
    implementation("javax.annotation:javax.annotation-api:1.3.2")

    // [Lombok]
    compileOnly("org.projectlombok:lombok")
    annotationProcessor("org.projectlombok:lombok")
    
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

sourceSets {
    main {
        java {
            srcDir("$buildDir/generated/source/proto/main/java")
            srcDir("$buildDir/generated/source/proto/main/grpc")
        }
    }
}
