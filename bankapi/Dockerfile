FROM amazoncorretto:17-alpine-jdk

COPY target/bankapi-0.0.1.jar bankapi.jar

WORKDIR /

ENTRYPOINT ["java", "-XX:+UseSerialGC", "-Xms256m", "-Xmx512m", "-jar", "/bankapi.jar"]