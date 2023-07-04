# 패키지 구조

## 유저 user / 공지 notice

유저, 공지 도메인은 헥사고날 아키텍처에 따라 패키지 구조를 정의하였습니다!

user
├── adapter
   ├── in
	    └── web
   └── out
       ├── authentication
       └── persistence
├── application
   ├── port
      ├── in
         ├── command
         ├── query
         └── response
      └── out
   └── service
└── domain

### 세부 정보

| **패키지명**                     | **이름**                     | **설명**                                                               |
|------------------------------|----------------------------|----------------------------------------------------------------------|
| adapter                      | 어댑터                        | 외부 세계와 포트 간 교환을 조정한다.                                                |
| adapter.in.web               | 인바운드 어댑터                   | 외부 애플리케이션/서비스와 내부 비즈니스 영역(인바운드 포트) 간 데이터 교환을 조정한다. Controller가 속해있다. |
| adapter.out                  | 아웃바운드 어댑터                  | 내부 비즈니스 영역(아웃바운드 포트)과 외부 애플리케이션/서비스 간 데이터 교환을 조정한다.                  |
| adapter.out.authentication   |                            | 유저 인증/인가 관련 어댑터                                                      |
| adapter.out.persistence      | 도메인 영속성 어댑터                | 도메인의 영속성은 외부와 직접적으로 소통하므로, 내부 비즈니스 영역과 분리가 필요하다.                     |
| application                  | 내부 비즈니스 영역                 |                                                                      |
| application.port             | 내부 비즈니스 영역을 외부 영역에 노출한 API |                                                                      |
| application.port.in          | 인바운드 포트                    | 내부 영역 사용을 위해 노출된 API                                                 |
| application.port.in.command  |                            | 상태를 변경하는 인바운드 포트                                                     |
| application.port.in.query    |                            | 상태를 반환하는 인바운드 포트                                                     |
| application.port.in.response |                            | 응답 DTO                                                               |
| application.port.out         | 아웃바운드 포트                   | 내부 영역이 외부 영역을 사용하기 위한 API                                            |
| application.service          |                            | 인바운드 포트 구현체 서비스                                                      |
| application.domain           | 비즈니스 도메인                   |                                                                      |

## 카테고리 category

category
├── controller
├── domain
├── dto
├── exception
├── repository
└── service

## 전시 기록 gallery

전시 기록은 전시(exhibition), 작품(artwork), 태그(tag) 등으로 이루어져있습니다.
하나의 전시(exhibition)은 여러 작품(artwork)를 가질 수 있고, 전시별로 정보를 작품별로 정보를 가질 수 있습니다. 또 하나의 전시에는 여러 태그(tag)를 추가할 수 있습니다.
이처럼 전시 기록은 다양한 기능과 도메인을 가진 핵심 개념입니다.

따라서, 전시 기록(gallery)이라는 애그리거트 하에, 전시 기록 단위 처리 로직과 도메인별 세부 로직으로 구성하였습니다.

gallery
├── controller
├── domain : 도메인별 세부 로직
   ├── entity
      ├── artwork
      └── exhibition
   └── repository
├── dto
   ├── artwork
   └── exhibition
├── exception
└── service