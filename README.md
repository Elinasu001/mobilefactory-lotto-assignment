# 로또 이벤트 시스템

SMS 인증 후 로또 번호를 발급받고, 발표 기간에 당첨 결과를 조회하는 이벤트 참여 시스템

<br>

## 기술 스택

| | |
|---|---|
| Language | Java 21 |
| Framework | Spring Boot 3.5.7 |
| ORM | MyBatis 3.0.5 |
| Database | Oracle DB (ojdbc11) |
| API 문서 | Swagger / OpenAPI 3.1 |
| Build | Gradle |

<br>

## 구현 기능

- 휴대폰 번호 인증 (Mock SMS, 유효시간 3분)
- 이벤트 참여 및 로또 번호 발급 (랜덤 6자리)
- 중복 참여 방지 (DB 유니크 인덱스)
- 등수별 당첨자 자동 생성 (1~4등 조건 기반)
- 당첨 결과 조회 (1차 등수 공개 / 2차 이후 당첨 여부만 표시)
- 이벤트 상태 자동 변경 스케줄러 (ACTIVE → CLOSED)
- 관리자 API (이벤트 생성 / 1등 강제 지정 / 당첨자 생성)

#### 미구현

- 현재 진행 이벤트 자동 조회 API (`GET /api/events/active`, `GET /api/events/announced`)
- 발표 10일 경과 미확인 당첨자 SMS 발송 스케줄러

<br>

## 문서

- [API 명세서](docs/로또이벤트_API명세서.pdf)
- [DB 설계서](docs/로또이벤트_DB설계.pdf)
- Swagger UI : `http://localhost:8080/swagger-ui/index.html`
