# 빌드 단계
FROM openjdk:21-slim AS build
WORKDIR /src
COPY . .
RUN chmod +x ./gradlew && ./gradlew clean build -x test

# 실행 단계
FROM openjdk:21-slim
WORKDIR /app
COPY --from=build /src/build/libs/*.jar app.jar
EXPOSE 8083
ENTRYPOINT ["java", "-jar", "app.jar"]