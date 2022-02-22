FROM openjdk:13-jdk-alpine
ARG JAR_FILE
COPY ${JAR_FILE} subscription-api.jar

ENTRYPOINT ["java","-jar","subscription-api.jar"]

