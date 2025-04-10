# 클린 + 레이어드 아키텍처 기반 프로젝트 구조

이 프로젝트는 클린 + 레이어드 아키텍처 원칙에 따라 구성되어 있으며, 각 계층은 다음과 같은 역할을 담당합니다.

##  계층 구조

- Domain: 비즈니스 핵심 로직 (가장 안쪽)
- Application: 유스케이스, 도메인 조합  
- Interface (Adapter): 사용자/외부 요청 수신 (Controller)
- Infrastructure: 기술적인 구현 (DB, 외부 API)

## 계층별 설명 

### 1. **Domain**
- 비즈니스 규칙과 핵심 로직이 위치합니다
- 외부 의존성 없습니다.
- 예시: `Product`, `Order`, `DiscountPolicy`

### 2. **Application**
- 유스케이스 흐름 => 비즈니스 동작
- 도메인 조합 및 입출력 포트 인터페이스 정의합니다
- 외부에서 주입받을 인터페이스를 명세합니다
- 예시: `ProductRepository`

### 3. **Interface (Adapter)**
- 외부 요청과 내부 유스케이스를 연결합니다
- Controller 및 요청/응답 DTO 포함
- 예시: `OrderController`, `OrderRequestDto`

### 4. **Infrastructure**
- 실제 기술 구현이 위치합니다.
- JPA Repository, 외부 시스템 연동 등
- 예시: `JpaProductRepository`

