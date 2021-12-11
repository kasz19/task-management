# syntax=docker/dockerfile:1doc

FROM openjdk:16-alpine3.13

WORKDIR /app

COPY src ./src
COPY application-config.yaml ./application-config.yaml
COPY .mvn/ .mvn
COPY mvnw pom.xml ./
RUN ./mvnw clean install

CMD java -jar target/task-crud-1.0-SNAPSHOT-shaded.jar server application-config.yaml

EXPOSE 8080