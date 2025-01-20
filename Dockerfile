FROM openjdk:17-jdk-slim

ARG JAR_FILE=trigo-0.0.1-SNAPSHOT.jar

COPY ${JAR_FILE} ./
COPY application-secret.yml ./

ENTRYPOINT ["java", "-jar", "trigo-0.0.1-SNAPSHOT.jar"]