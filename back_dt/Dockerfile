# 베이스 이미지 설정 (OpenJDK 11)
FROM openjdk:17-jdk-slim

# 빌드 타임 변수 설정
ARG APP_HOME=/app
ARG APP_JAR=demo-0.0.1-SNAPSHOT.jar
ARG APP_TARGET=build/libs

WORKDIR $APP_HOME

# 애플리케이션 jar 파일을 컨테이너의 /app 디렉토리에 복사
COPY $APP_TARGET/$APP_JAR $APP_HOME/$APP_JAR

# 애플리케이션 실행 명령어 설정
CMD ["java", "-jar", "demo-0.0.1-SNAPSHOT.jar"]
