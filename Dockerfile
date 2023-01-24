# 도커 허브에서 이미지를 가져와서 이미지를 작업한다
# FROM (이미지 이름:버전)
FROM openjdk:11

# 컨테이너 실행 전 작동할 명령
# RUN (명령)
# 타임존 설정 (설정을 하지 않으면 시간 저장시 다른 시간대로 저장됨)
RUN ln -snf /usr/share/zoneinfo/Asia/Seoul /etc/localtime
RUN echo Asia/Seoul > /etc/timezone

# 컨테이너 내 작업 경로
# WORKDIR (경로)
 WORKDIR /app

# 작업 파일을 변수화 하기
# ARG (변수명)=(파일명)
ARG JAR_FILE=build/libs/*.jar
ARG FIREBASE_JSON=firebase.json
ARG ACTIVE_PROFILE=prod
ENV profile=$ACTIVE_PROFILE
RUN echo $profile

# 작업 파일을 컨테이너로 복사
# COPY (파일명 또는 ${변수명}) (복사할 파일명)
COPY ${JAR_FILE} app.jar
COPY ${FIREBASE_JSON} firebase.json

# 컨테이너 시작 시 내릴 명령 (CMD와 ENTRYPOINT 차이 확인)
# ENTRYPOINT [(명령),(매개변수),(매개변수),(...)]
ENTRYPOINT ["java","-jar","app.jar"]