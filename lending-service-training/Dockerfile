FROM openjdk:8-alpine
COPY target/lending-service-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java","-Dserver.port=8080", "-jar","/app.jar"]