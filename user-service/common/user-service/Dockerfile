FROM openjdk:21

ARG FILE_JAR=target/user-service-2.0.jar

ADD ${FILE_JAR} user-service.jar

ENTRYPOINT ["java", "-jar", "user-service.jar"]

EXPOSE 8081