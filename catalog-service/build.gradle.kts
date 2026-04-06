plugins {
    id("java")
    id("org.springframework.boot")
    id("org.openapi.generator") version "7.10.0"
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-validation")
    implementation("org.springframework.boot:spring-boot-starter-actuator")
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.7.0")

    // Swagger UI / OpenAPI Annotations
    implementation("io.swagger.core.v3:swagger-annotations:2.2.27")

    // Jackson
    implementation("org.openapitools:jackson-databind-nullable:0.2.6")

    testImplementation("org.springframework.boot:spring-boot-starter-test")
}

val openApiGenerate by tasks.registering(org.openapitools.generator.gradle.plugin.tasks.GenerateTask::class) {
    generatorName.set("spring")
    inputSpec.set("$projectDir/src/main/resources/api/catalog-api.yaml")
    outputDir.set("$buildDir/generated/")
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
            srcDir("$buildDir/generated/src/main/java")
        }
    }
}

tasks.named("compileJava") {
    dependsOn("openApiGenerate")
}
