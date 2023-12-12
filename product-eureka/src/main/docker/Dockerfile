# Builder stage
# Java image for building the application
FROM amazoncorretto:17.0.7-alpine as builder
WORKDIR application

# Add Spring Boot application JAR file from Maven
ADD maven/${project.build.finalName}.jar ${project.build.finalName}.jar

# Extract the layers from the JAR for Docker Layering
RUN java -Djarmode=layertools -jar ${project.build.finalName}.jar extract

FROM amazoncorretto:17.0.7-alpine

# Label image with project information
LABEL PROJECT_NAME=${project.artifactId} \
      PROJECT=${project.id}

EXPOSE 8080

# Container's work directory
WORKDIR application

# Copy the layers from the builder stage for image layer caching
COPY --from=builder application/dependencies/ ./
COPY --from=builder application/spring-boot-loader/ ./
COPY --from=builder application/snapshot-dependencies/ ./
COPY --from=builder application/application/ ./

# Entrypoint to the Spring Boot JarLauncher with security random configuration
ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom", "org.springframework.boot.loader.JarLauncher"]