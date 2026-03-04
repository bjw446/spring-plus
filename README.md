# SPRING PLUS

---

## 기술 스택

- Java 17
- Spring Boot
- Spring Data JPA
- QueryDSL
- Spring Security
- JWT
- H2 / MySQL
- JUnit5

---

## 구현 사항

### 1. @Transactional 개선

- `/todos` 저장 API 호출 시 readOnly 트랜잭션으로 인해 데이터 저장이 불가능한 문제가 발생.
- 저장 메서드에 `@Transactional`을 명시하여 쓰기 트랜잭션으로 수정.
- readOnly 옵션이 적용되지 않도록 구조 개선.

---

### 2. JWT 확장 (nickname 추가)

- User 테이블에 `nickname` 컬럼 추가 (중복 허용).
- JWT 생성 시 nickname을 claim에 포함.
- 토큰 파싱 시 nickname 추출 가능하도록 수정.
- 프론트엔드에서 닉네임 사용 가능.

---

### 3. JPA 검색 기능 개선

- weather 조건 검색 (선택 조건)
- 수정일 기준 기간 검색 (시작/종료 선택 가능)
- JPQL을 사용하여 동적 조건 처리.

---

### 4. 컨트롤러 테스트 수정

- 존재하지 않는 Todo 조회 시 테스트 실패 문제 수정.
- 예외 발생 시 적절한 HTTP 상태 코드 반환하도록 수정.
- 테스트 코드 기대값과 일치하도록 보완.

---

### 5. AOP 수정

- `UserAdminController.changeUserRole()` 실행 전 AOP가 동작하도록 Pointcut 표현식 수정.
- 개발 의도에 맞게 Admin 접근 로그 정상 동작.

---

### 6. JPA Cascade 적용

- Todo 생성 시 작성자가 자동으로 Manager로 등록되도록 구현.
- `CascadeType.PERSIST`를 활용하여 Todo 저장 시 Manager도 함께 저장.

---

### 7. N+1 문제 해결

- Comment 조회 시 발생하던 N+1 문제 해결.
- Fetch Join을 사용하여 연관 엔티티를 한 번에 조회하도록 수정.

---

### 8. QueryDSL 적용 (단건 조회)

- JPQL 기반 조회 로직을 QueryDSL로 변경.
- fetchJoin을 사용하여 N+1 문제 방지.

---

### 9. Spring Security 도입

- 기존 Filter + ArgumentResolver 구조 제거.
- Spring Security 기반 인증/인가 구조로 변경.
- JWT 기반 토큰 인증 유지.
- 권한 처리는 Security 기능을 활용.

---

### 10. QueryDSL 검색 API 구현

새로운 검색 API 구현.

- 제목 부분 검색 (LIKE)
- 생성일 범위 검색
- 담당자 닉네임 부분 검색
- 최신순 정렬
- 페이징 처리
- Projections를 활용하여 필요한 필드만 반환
    - 일정 제목
    - 담당자 수
    - 댓글 개수

---

### 11. Transaction 심화

매니저 등록 실패 여부와 관계없이 로그가 항상 저장되도록 구현.

- Log 테이블 생성
- `saveManager()`는 기본 트랜잭션 사용
- `saveLog()`는 `@Transactional(propagation = REQUIRES_NEW)` 적용
- 매니저 등록 실패 시 롤백되더라도 로그는 별도 트랜잭션으로 저장

---

## 학습 내용

- @Transactional readOnly 동작 이해
- 트랜잭션 전파(REQUIRES_NEW) 개념 이해
- JWT Claim 확장 방법
- JPQL 및 QueryDSL 활용
- Fetch Join을 통한 N+1 문제 해결
- Cascade 동작 원리 이해
- Spring Security 인증/인가 구조 이해

---

## 테스트 코드

과제에서 요구한 테스트 코드를 기준으로 작성하였으며,  
기능 정상 동작 및 예외 상황을 검증하였습니다.