FROM java:8-jdk-alpine
COPY eureka-server-0.0.1.jar /usr/app/
WORKDIR /usr/app
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "eureka-server-0.0.1.jar"]