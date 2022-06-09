FROM openjdk:8-alpine
COPY target/eureka-server-0.0.1-SNAPSHOT.jar app.jar
ENTRYPOINT ["java","-Dserver.port=8761", "-jar","/app.jar"]

