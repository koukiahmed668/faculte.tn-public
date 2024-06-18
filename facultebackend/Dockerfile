FROM maven:3.8.5-openjdk-17 AS build
WORKDIR /app
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

FROM openjdk:17.0.1-jdk-slim
WORKDIR /app
COPY --from=build /app/target/facultebackend-0.0.1-SNAPSHOT.jar app.jar
COPY src/main/resources/client_secrets.json src/main/resources/client_secrets.json
COPY src/main/resources/credentials.json src/main/resources/credentials.json
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]
