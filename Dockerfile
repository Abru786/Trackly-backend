#FROM eclipse-temurin:17-jdk
#
#WORKDIR /app
#
#COPY .mvn .mvn
#COPY mvnw .
#COPY pom.xml .
#
#
#RUN chmod +x mvnw
#
#RUN ./mvnw dependency:go-offline
#
#COPY src src
#
#RUN ./mvnw clean package -DskipTests
#EXPOSE 8080

FROM maven:3.9.8-eclipse-temurin-17 AS build

WORKDIR /app

COPY pom.xml .
COPY src ./src

RUN mvn clean package -DskipTests

FROM eclipse-temurin:17-jre

WORKDIR /app

COPY --from=build /app/target/*.jar trackly-app.jar

EXPOSE 8080


ENTRYPOINT ["java","-jar","trackly-app.jar"]