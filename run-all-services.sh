#!/bin/bash

# 공통 서비스 실행
echo "==== Starting common services (Kafka, Zookeeper, Redis) ===="
docker-compose --env-file .env up -d
if [ $? -eq 0 ]; then
  echo "Common services started successfully!"
else
  echo "Error starting common services. Check logs for more details."
  exit 1
fi

# 개별 서비스 목록 및 상대 경로 설정
services=("eureka" "gateway" "auth" "user" "post" "bid" "delivery" "payment")
base_dir="$(pwd)" # 현재 경로를 기준으로 설정

# 루트 환경 변수 파일 경로
COMMON_ENV_FILE="$base_dir/.env"

# 모든 서비스 실행
for service in "${services[@]}"; do
  echo "==== Starting $service services===="
  service_dir="$base_dir/$service"

  # 서비스 디렉토리로 이동
  if [ -d "$service_dir" ]; then
    cd "$service_dir" || { echo "Directory $service not found!"; exit 1; }
  else
    echo "Service directory $service_dir does not exist! Skipping..."
    continue
  fi

  # Docker Compose 실행
  docker-compose --env-file "$COMMON_ENV_FILE" --env-file .env up -d

  # 실행 결과 확인
  if [ $? -eq 0 ]; then
    echo "$service started successfully!"
  else
    echo "Error starting $service. Check logs for more details."
    exit 1
  fi

  # 원래 디렉토리로 이동
  cd "$base_dir" || exit
done

echo "==== All services are up and running! ===="
