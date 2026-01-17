FROM eclipse-temurin:17-jre-alpine
WORKDIR /app
COPY target/demo-2-0.0.4-SNAPSHOT.jar app.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "app.jar"]