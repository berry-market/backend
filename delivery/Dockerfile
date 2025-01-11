# 1. 빌드 단계
FROM openjdk:17-slim AS build

# 작업 디렉터리 생성
WORKDIR /app

# 소스 코드 복사
COPY . .

# Gradle 빌드 실행
RUN ./gradlew clean build -x test

# 2. 실행 단계
FROM openjdk:17-slim

# 작업 디렉터리 설정
WORKDIR /app

# 빌드된 JAR 파일 복사
COPY --from=build /app/build/libs/delivery-0.0.1-SNAPSHOT.jar delivery-0.0.1-SNAPSHOT.jar

# 애플리케이션 실행
ENTRYPOINT ["java", "-jar", "delivery-0.0.1-SNAPSHOT.jar"]
