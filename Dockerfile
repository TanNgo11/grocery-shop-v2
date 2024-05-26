FROM openjdk:17

COPY . .
RUN mvn clean package -Pprod -DskipTests


ARG JAR_FILE=target/*.jar

ADD ${JAR_FILE} identity-service.jar

ENTRYPOINT ["java", "-jar", "identity-service.jar"]

EXPOSE 80