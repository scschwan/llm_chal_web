#!/bin/bash

PROJECT_DIR="/home/dmillion/llm_chal_web"
JAR_DIR="$PROJECT_DIR/build/libs"

cd $PROJECT_DIR || exit

echo "1. 최신 코드로 업데이트(git pull)"
git pull

echo "2. BootJar 빌드"
./gradlew bootJar -x test

echo "3. 기존 Spring Boot 프로세스 종료"
PID=$(pgrep -f '.jar')
if [ -n "$PID" ]; then
  kill -9 $PID
  echo "기존 프로세스 종료 완료: $PID"
else
  echo "실행 중인 프로세스 없음"
fi

echo "4. 새 jar 실행"
JAR_FILE=$(ls $JAR_DIR/*.jar | grep -v plain | head -n 1)
nohup java -jar $JAR_FILE > $PROJECT_DIR/nohup.out 2>&1 &