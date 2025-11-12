FROM maven:3.9.6-eclipse-temurin-17 AS build

WORKDIR /app

# Copy only the service pom.xml first for dependency caching
COPY pom.xml .

# Copy the whole source code
COPY . .

# Build the project
RUN mvn -q clean package -DskipTests

# Final stage
FROM eclipse-temurin:17-jdk-alpine

WORKDIR /app

# Copy the jar from the build stage
COPY --from=build /app/target/notification-service-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8084

ENTRYPOINT ["java", "-jar", "app.jar"]
