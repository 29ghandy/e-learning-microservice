# --------------------------
# Build stage
# --------------------------
FROM maven:3.9.6-eclipse-temurin-17 AS build

WORKDIR /app

# Copy only the pom.xml first for caching dependencies
COPY pom.xml .

# Copy the rest of the source code
COPY . .

# Build the Maven project
RUN mvn clean package -DskipTests

# --------------------------
# Run stage
# --------------------------
FROM eclipse-temurin:17-jdk-alpine

WORKDIR /app

# Copy the built jar from the build stage
COPY --from=build /app/target/gateway-service-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
