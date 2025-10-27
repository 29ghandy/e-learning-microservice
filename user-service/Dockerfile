FROM maven:3.9.6-eclipse-temurin-17 AS build

WORKDIR /app


COPY pom.xml .


COPY user-service/pom.xml user-service/pom.xml


COPY . .

RUN mvn -q -pl user-service -am clean package -DskipTests

FROM eclipse-temurin:17-jdk-alpine

WORKDIR /app

COPY --from=build /app/user-service/target/user-service-0.0.1-SNAPSHOT.jar app.jar

EXPOSE 8081

ENTRYPOINT ["java", "-jar", "app.jar"]