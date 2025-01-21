FROM openjdk:17-jdk-slim
WORKDIR /home/ubuntu/app

COPY trigo-0.0.1-SNAPSHOT.jar .
ENTRYPOINT ["java", "-jar", "trigo-0.0.1-SNAPSHOT.jar"]