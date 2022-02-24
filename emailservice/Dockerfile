FROM openjdk:13-jdk-alpine
ARG JAR_FILE
COPY ${JAR_FILE} email-service.jar

ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom","-jar","email-service.jar"]

