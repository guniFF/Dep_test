# backend/Dockerfile
FROM openjdk:11-jre-slim
WORKDIR /app
COPY ./target/backend.jar ./backend.jar
ENTRYPOINT ["java", "-jar", "backend.jar"]
