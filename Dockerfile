FROM openjdk:17-jdk-slim
WORKDIR /home/ubuntu/app

EXPOSE 8081

ENTRYPOINT ["java", "-jar", "trigo-0.0.1-SNAPSHOT.jar"]