FROM openjdk:13-jdk-alpine
ARG JAR_FILE
COPY ${JAR_FILE} subscription-api.jar

ENTRYPOINT ["java","-Djava.security.egd=file:/dev/./urandom", "-Dspring.profiles.active=compose", "-jar","subscription-api.jar"]

