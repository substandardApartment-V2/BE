FROM openjdk:21-jdk-slim

# 시간대 설정을 위한 패키지 설치 및 서울 시간대 설정
RUN apt-get update && apt-get install -y \
    tzdata \
    && ln -snf /usr/share/zoneinfo/Asia/Seoul /etc/localtime \
    && echo "Asia/Seoul" > /etc/timezone \
    && dpkg-reconfigure -f noninteractive tzdata \
    && rm -rf /var/lib/apt/lists/*

ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} app.jar

# Chromium 설치 (Selenium 크롤링을 위해 필요, chromium-driver는 제외)
RUN apt-get update && apt-get install -y \
    chromium \
    && rm -rf /var/lib/apt/lists/*

# 다운로드 및 사용자 데이터 디렉토리 생성 및 권한 설정
RUN mkdir -p /tmp/downloads /tmp/chrome-user-data /webdriver-cache \
    && chmod -R 755 /tmp /webdriver-cache

# ChromeDriver 경로 환경 변수 설정
ENV CHROME_DRIVER_PATH=/usr/bin/chromedriver

# wait-for-it.sh 복사 및 실행 권한 설정
COPY wait-for-it.sh wait-for-it.sh
RUN chmod +x wait-for-it.sh

# MySQL이 준비될 때까지 기다린 후 애플리케이션 실행
ENTRYPOINT ["/wait-for-it.sh", "mysql_db:3306", "--timeout=60", "--", "java", "-jar", "/app.jar"]