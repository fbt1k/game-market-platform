FROM maven:3.9.11-eclipse-temurin-21 AS build
WORKDIR /build
COPY . .
RUN mvn -pl notifications-service -am clean package -DskipTests

FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY --from=build /build/notifications-service/target/*.jar app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
