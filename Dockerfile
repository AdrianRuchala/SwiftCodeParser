FROM gradle:8.0-jdk17 AS build
COPY . /app

WORKDIR /app
RUN ./gradlew build -x test

FROM openjdk:17-jdk
WORKDIR /app
COPY --from=build /app/build/libs/*.jar app.jar
EXPOSE 8080
CMD ["java", "-Dspring.config.location=classpath:/application.properties", "-jar", "app.jar"]

