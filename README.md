# 아르티 백엔드 서버 (all-rounder team2) 🛠

[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)

## 1. 주요 스택

- spring boot:2.7.6
- jdk:11
- mysql:8.0

## 2. 커밋 메시지

커밋 메시지 형식은 아래와 같습니다. `subject`까지만 작성해도 괜찮습니다.

```
<type>(<scope>): <subject>
<BLANK LINE>
<body>
<BLANK LINE>
<footer>
```

## 2.1 커밋 타입(`<Type>`)

- `feat` :  (feature)
- `fix` :  (bug fix)
- `docs`:  (documentation)
- `style` : (formatting, missing semi colons, …)
- `refactor`
- `test` : (when adding missing tests)
- `chore` : (maintain)

## 3. 패키지 구조

- `module`
    - `controller`
    - `domain`
    - `dto`
    - `exception`
    - `repository`
    - `service`

## 4. 인텔리제이 자동 재시작 설정

1. 설정  >  빌드, 실행, 배포  >  컴파일러  > 프로젝트 자동 빌드 체크
2. 고급 설정 > 컴파일러 > 개발된 애플리케이션이 현재 실행 중인 경우에도 auto-make가 시작되도록 허용 체크

## 5. 실행

데이터베이스 실행 :

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

profile 환경변수 설정 :

1. 인텔리제이 메뉴 Run > Edit Configurations 설정 접속 혹은 스크린 샷과 같이 접속
   <img width="863" alt="스크린샷 2023-01-07 오후 4 06 05" src="https://user-images.githubusercontent.com/42285463/211137975-87d0e79c-7f8b-4640-9eae-0ad03d68fef5.png">
2. Active profiles에 develop 입력 ( 개발용의 경우 develop, production의 경우 prod 입력 )
   <img width="1042" alt="스크린샷 2023-01-07 오후 4 07 21" src="https://user-images.githubusercontent.com/42285463/211138359-e071c6ff-6fa5-432e-87e0-101e759b6037.png">

환경 변수 추가 및 변경 시 ( production ) :

- application-dev.yml에 해당하는 환경변수를 추가하고, 환경변수 구조 파악을 위하여 application-prod.yml에 추가되는 환경변수의 이름을 추가.
- 실제 환경변수 값이 포함된 application-prod.yml을 base 64로 인코딩하여 Github Secrets에 업데이트

## 6. 배포

- Github actions를 시범 적용 중으로, ALB 이슈가 있어, Github Actions Workflow 중 기존 ECS Task를 중단해야함.
- 현재 배포 시에 Github Secrets에 base64로 인코딩하여 저장해둔 application-prod.yml과 firebase.json을 decode하여 생성 후
  빌드하는 과정을 포함하고 있음.

## 7. 기여자

| Avatar                                                                                         | Name | Team    | 
|------------------------------------------------------------------------------------------------|------|---------|
| <img src="https://avatars.githubusercontent.com/u/42285463?v=4" width="100px" height="100px"/> | 마민지  | 올라운더 2팀 |
| <img src="https://avatars.githubusercontent.com/u/39932141?v=4" width="100px" height="100px"/> | 이하늘  | 올라운더 2팀 | 
