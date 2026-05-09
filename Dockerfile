# Multi-stage build for Java Judge Server
# Build stage
FROM maven:3.9-eclipse-temurin-17 AS builder
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# Runtime stage
FROM eclipse-temurin:17-jre
WORKDIR /app
COPY --from=builder /app/target/judge-server-1.0.0.jar judge-server.jar
RUN mkdir -p /app/data
EXPOSE 2230 2240
ENTRYPOINT ["java", "-jar", "judge-server.jar"]
