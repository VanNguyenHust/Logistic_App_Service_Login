FROM openjdk:21

ARG FILE_JAR=target/api-gateway-2.0.jar

ADD ${FILE_JAR} api-gateway.jar

ENTRYPOINT ["java", "-jar", "api-gateway.jar"]

EXPOSE 9000