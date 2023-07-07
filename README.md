<div align="center">
  <br>
  <h2> 간편한 전시 관람 서비스</h2>
  <h1> 아르티 ARTIE 🎨 </h1>
  <strong>API 서버 레포지토리</strong>
</div>
<br>

## 프로젝트 소개

아르티는 전시 중 관람기록을 간편하게 남길 수 있도록 도와주며 기록해둔 전시를 찾기 쉽게 도와주는 전시 기록 서비스입니다.

소중한 영감을 한 눈에 볼 수 있도록 작품별로 기록하고, 전시 링크를 담아 나만의 기록물을 만들어보아요!

작품마다 작품 명, 작가 명을 기록하고 그 순간의 감정을 기록하며, 각각의 전시는 나만의 카테고리로 분류할 수 있어요.

[🤖 안드로이드 플레이스토어 🤖](https://play.google.com/store/apps/details?id=com.yapp.gallery)

![프로젝트 소개](https://user-images.githubusercontent.com/70252417/234887246-25960d0e-f54a-4223-9f35-1ea246303358.png)

### 프로젝트 기능
[서비스 세부 기능](/docs/service_detail.md)

## 기술 스택

- spring boot:2.7.6
- jdk:11
- mysql:8.0
- jpa
- firebase authentication
- aws : ecs, ecr, alb, rds, s3, cloudfront

## 서버 아키텍처

![서버 아키텍처](https://github.com/akalswl14/coding-test/assets/42285463/4f0eff30-fb90-44d2-aa8b-531f5e1be89e)

## 배포 파이프라인

![CD 아키텍처](https://github.com/akalswl14/coding-test/assets/42285463/733ee4b2-3224-4281-ab1d-7cee10e7465e)

## 기여자

| Avatar                                                                                         | Name   | Team         | 개발 기간 |
| ---------------------------------------------------------------------------------------------- | ------ | ------------ | ------- |
| <img src="https://avatars.githubusercontent.com/u/42285463?v=4" width="100px" height="100px"/> | 마민지 | 올라운더 2팀 | 2022.12 ~ ing |
| <img src="https://avatars.githubusercontent.com/u/39932141?v=4" width="100px" height="100px"/> | 이하늘 | 올라운더 2팀 | 2022.12 ~ ing |

## 실행

### 서버 실행

```shell
$ ./gradlew clean build
$ java -jar /build/libs/artie-0.0.1-SNAPSHOT.jar
```

### 데이터베이스 실행 (docker-compose)

```shell
# mysql 실행
$ cd docker
$ docker-compose up --build
```

```shell
# mysql 종료
$ cd docker
$ docker-compose down
```

## 패키지 구조

[패키지 구조 설명](/docs/package_structure.md)

## 컨벤션
[컨벤션 문서](docs/convention.md)

## 개발 환경 설정
[개발 환경 문서](/docs/dev_setting.md)