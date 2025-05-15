# 선착순 쿠폰 Redis 통합 테스트 설계 및 구현 보고서

##  개요

설계는 쿠폰의 **발급**, **재고 관리**, **만료 처리**를 중심으로, **중복 방지**와 **데이터 일관성**을 보장하는 구조입니다.

---

## 주요 기능

### 1. 쿠폰 발급 (`issueCouponRedis`)

* **목적**: 선착순으로 쿠폰을 발급하며, 중복 발급을 방지하고 재고를 관리
* **Redis Key 구조**:

    * **재고 관리**: `coupon:{couponId}:stock`
    * **발급 내역**: `coupon:{couponId}:issued`
* **데이터 모델**: Coupon, IssuedCoupon 엔티티

```java
// 쿠폰 발급 로직
@Transactional
public void issueCouponRedis(long userId, long couponId) {
    String stockKey = String.format(COUPON_STOCK_KEY, couponId);
    String issuedKey = String.format(COUPON_ISSUED_KEY, couponId);

    // 중복 발급 체크
    Boolean isIssued = redisTemplate.opsForSet().isMember(issuedKey, String.valueOf(userId));
    if (Boolean.TRUE.equals(isIssued)) {
        throw new IllegalStateException("이미 발급받은 쿠폰입니다.");
    }

    // 재고 감소
    String couponNumber = redisTemplate.opsForList().leftPop(stockKey);
    if (couponNumber == null) {
        throw new IllegalStateException("쿠폰이 소진되었습니다.");
    }

    // 발급 기록 저장
    redisTemplate.opsForSet().add(issuedKey, String.valueOf(userId));
}
```

### 2. 데이터 영속화 (`persistIssuedUserInfo`)

* **목적**: Redis에 저장된 발급 내역을 주기적으로 DB로 영속화하여 데이터 일관성 유지
* **스케줄링**: 매일 자정 실행
* **데이터 모델**: IssuedCoupon 엔티티

```java
@Scheduled(cron = "0 0 0 * * ?")
@Transactional
public void persistIssuedUserInfo() {
    Set<String> issuedKeys = redisTemplate.keys("coupon:*:issued");
    if (issuedKeys == null || issuedKeys.isEmpty()) return;

    for (String issuedKey : issuedKeys) {
        Long couponId = Long.valueOf(issuedKey.split(":")[1]);

        // 발급된 사용자 목록 조회
        Set<Object> userIdObjects = redisTemplate.opsForSet().members(issuedKey);
        if (userIdObjects == null || userIdObjects.isEmpty()) continue;

        // DB에 저장
        List<IssuedCoupon> newIssuedCoupons = userIdObjects.stream()
                .map(userId -> IssuedCoupon.builder()
                        .userId(Long.valueOf(userId.toString()))
                        .coupon(couponRepository.findById(couponId).orElseThrow())
                        .issuedAt(LocalDateTime.now())
                        .build())
                .collect(Collectors.toList());

        issuedCouponRepository.saveAll(newIssuedCoupons);

        // Redis 데이터 삭제
        redisTemplate.delete(issuedKey);
    }
}
```

### 3. 만료 처리 (`IssuedCoupon`)

* **목적**: 발급된 쿠폰의 만료일을 정확히 관리
* **데이터 모델**: IssuedCoupon 엔티티

```java
public boolean isValid() {
    return !isUsed && expiredAt.isAfter(LocalDateTime.now());
}

public void markAsUsed() {
    this.isUsed = true;
}
```

---

##  통합 테스트 시나리오

###  테스트 항목

1. **정상 발급**
2. **중복 발급 방지**
3. **재고 부족 처리**
4. **데이터 영속화**
5. **만료일 검증**

### 테스트 파일 (`CouponServiceIntegrationTest.java`)

```java
@Test
@Transactional
void testIssueCouponRedis_Success() {
    couponService.issueCouponRedis(101L, testCoupon.getId());
    couponService.issueCouponRedis(102L, testCoupon.getId());

    Set<Object> issuedUsers = redisTemplate.opsForSet().members(String.format(COUPON_ISSUED_KEY, testCoupon.getId()));
    assertThat(issuedUsers).contains("101", "102");
}

@Test
@Transactional
void testIssueCouponRedis_Duplicate() {
    couponService.issueCouponRedis(201L, testCoupon.getId());
    assertThatThrownBy(() -> couponService.issueCouponRedis(201L, testCoupon.getId()))
            .isInstanceOf(IllegalStateException.class)
            .hasMessageContaining("이미 발급받은 쿠폰입니다.");
}

@Test
@Transactional
void testIssueCouponRedis_StockDepleted() {
    for (int i = 1; i <= 10; i++) {
        couponService.issueCouponRedis(300L + i, testCoupon.getId());
    }

    assertThatThrownBy(() -> couponService.issueCouponRedis(400L, testCoupon.getId()))
            .isInstanceOf(IllegalStateException.class)
            .hasMessageContaining("쿠폰이 소진되었습니다.");
}
```

---

##  최적화 및 개선 방향

1. **Batch Insert** - 대량의 데이터를 한 번에 저장하여 성능 최적화
2. **Pipeline 사용** - Redis 처리 최적화
3. **캐시 일관성 관리** - TTL 만료 전 데이터 보존을 위한 메커니즘 개선

---
