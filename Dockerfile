# Multi-stage Dockerfile for slot-game-engine-service
# Build stage
FROM eclipse-temurin:21-jdk-jammy AS build
WORKDIR /app
COPY pom.xml .
COPY src src
RUN apt-get update && apt-get install -y --no-install-recommends maven && rm -rf /var/lib/apt/lists/*
RUN mvn -B package -DskipTests

# Runtime stage
FROM eclipse-temurin:21-jre-jammy
WORKDIR /app
COPY --from=build /app/target/*.jar app.jar
EXPOSE 8084
ENTRYPOINT ["java", "-jar", "app.jar"]
