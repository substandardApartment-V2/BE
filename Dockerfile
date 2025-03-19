FROM openjdk:21

ARG JAR_FILE=build/libs/*.jar
COPY ${JAR_FILE} app.jar
ENTRYPOINT ["java","-jar","/app.jar"]

# wait-for-it.sh 복사 및 실행 권한 설정
COPY wait-for-it.sh wait-for-it.sh
RUN chmod +x wait-for-it.sh

# MySQL이 준비될 때까지 기다린 후 애플리케이션 실행(대기시간 30초)
ENTRYPOINT ["/wait-for-it.sh", "mysql_db:3306", "--timeout=30", "--", "java", "-jar", "/app.jar"]