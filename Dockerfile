FROM openjdk:17

ARG JAR_FILE=target/*.jar

ADD ${JAR_FILE} identity-service.jar

ENTRYPOINT ["java", "-jar", "identity-service.jar"]

EXPOSE 80