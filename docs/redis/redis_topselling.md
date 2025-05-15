# Top Selling Product 설계 및 보고서

##  개요
인기 상품 시스템은 실시간 판매량 집계와 인기 상품 순위를 효율적으로 관리하기 위해 설계되었습니다. 이 시스템은 판매 데이터의 정확성과 빠른 조회 성능을 보장하기 위해 Redis Sorted Set을 핵심 데이터 구조로 사용합니다.

Redis 자료구조 선택 이유

Sorted Set: 판매량 집계와 자동 정렬 관리 (빠른 순위 계산)

원자적 점수 증가: 동시성 문제 없이 판매량을 정확하게 집계

범위 조회 최적화: ZRANGE, ZREVRANGE를 통한 빠른 범위 조회

이 구조를 통해 실시간 집계, 정확한 순위 계산, 데이터 일관성을 유지하며, 주기적인 DB 영속화로 데이터를 안정적으로 관리할 수 있습니다.
---

##  주요 기능

### 1. 판매량 집계 (`increaseSalesCount`)

* **목적**: 각 상품의 판매량을 Redis에 기록하여 실시간으로 인기 상품을 집계
* **Redis Key 구조**: `popular:daily:{yyyy-mm-dd}`
* **데이터 모델**: TopSellingProduct 엔티티
* **TTL 설정**: 24시간 (하루 단위 집계)

```java
// 판매량 증가
public void increaseSalesCount(List<OrderItem> items) {
    String dailyKey = DAILY_POPULAR_KEY + LocalDate.now();
    for (OrderItem item : items) {
        long productId = item.getProduct().getId();
        int quantity = item.getQuantity();
        redisTemplate.opsForZSet().incrementScore(dailyKey, String.valueOf(productId), quantity);
    }
    redisTemplate.expire(dailyKey, Duration.ofDays(1));
}
```

### 2. 데이터 영속화 (`persistDailyPopularProducts`)

* **목적**: Redis에 저장된 판매량 데이터를 주기적으로 DB로 영속화
* **스케줄링**: 매일 자정 실행
* **데이터 모델**: TopSellingProduct 엔티티

```java
@Scheduled(cron = "0 0 0 * * ?")
@Transactional
public void persistDailyPopularProducts() {
    String dailyKey = DAILY_POPULAR_KEY + LocalDate.now().minusDays(1);
    Set<String> productIds = redisTemplate.opsForZSet().reverseRange(dailyKey, 0, -1);

    if (productIds != null) {
        for (String productIdStr : productIds) {
            Long productId = Long.parseLong(productIdStr);
            Double salesCount = redisTemplate.opsForZSet().score(dailyKey, productIdStr);

            if (salesCount == null) continue;

            TopSellingProduct topSellingProduct = topSellingProductRepository
                    .findByProductIdAndAggregatedAt(productId, LocalDate.now().minusDays(1))
                    .orElseGet(() -> new TopSellingProduct(productId, salesCount.intValue(), LocalDate.now().minusDays(1)));

            topSellingProductRepository.save(topSellingProduct);
        }
    }
    redisTemplate.delete(dailyKey);
}
```

### 3. 인기상품 조회 (`getTopSellingProducts`)

* **목적**: 인기 상품의 상위 10개를 반환
* **DB 조회**: TopSellingProduct 테이블에서 조회

```java
public List<TopSellingProduct> getTop10PopularProducts() {
    return topSellingProductRepository.findTop10ByOrderByTotalSoldDesc();
}
```

---

##  통합 테스트 시나리오

###  테스트 항목

1. **판매량 증가 테스트**
2. **데이터 영속화 테스트**
3. **TTL 설정 검증**
4. **Top10 조회 테스트**

###  테스트 파일 (`TopSellingProductIntegrationTest.java`)

```java
@Test
void testIncreaseSalesCount() {
    Product productA = productRepository.findByName("상품A").orElseThrow();
    Product productB = productRepository.findByName("상품B").orElseThrow();
    productService.increaseSalesCount(List.of(
            new OrderItem(productA, 10),
            new OrderItem(productB, 5)
    ));
    String dailyKey = DAILY_POPULAR_KEY + LocalDate.now();
    Double scoreA = redisTemplate.opsForZSet().score(dailyKey, String.valueOf(productA.getId()));
    Double scoreB = redisTemplate.opsForZSet().score(dailyKey, String.valueOf(productB.getId()));
    assertThat(scoreA).isEqualTo(10.0);
    assertThat(scoreB).isEqualTo(5.0);
}
```

---

##  최적화 및 개선 방향

1. **Batch Insert** - 대량의 데이터를 한 번에 저장하여 성능 최적화
2. **Pipeline 사용** - Redis 처리 최적화
3. **캐시 일관성 관리** - TTL 만료 전 데이터 보존을 위한 개선

---

