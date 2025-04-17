###  k6 부하 테스트 및 개선 사항


## 테스트 개요

| 항목 | 내용 |
|------|------|
| 테스트 도구 | k6 |
| 동시 사용자 수 (VUs) | 50명 |
| 테스트 시간 | 30초 (평균 1초 단위 요청) |
| 대상 API |
| - /products/{id} | 상품 단건 조회 |
| - /products | 상품 다건 조회  |
| - /products/popular | 인기 상품 조회 (정렬 + 조인) |



---

## 부하 테스트 결과 요약

### 1. GET /products/{id}

| 항목 | 결과 |
|------|------|
| 총 요청 수 | 8964 |
| 성공률 | 100% |
| 평균 응답 시간 | 12.12ms |
| 최대 응답 시간 | 52ms |
| p(90) | 16.85ms |
| p(95) | 18.6ms |
| 결론 | **병목 없음** |

---

### 2. GET /products/popular

| 항목 | 결과 |
|------|------|
| 총 요청 수 | 8940 |
| 성공률 | 100%
| 평균 응답 시간 | 14.8ms |
| 최대 응답 시간 | ⚠️ 106.5ms |
| p(90) | 20.41ms |
| p(95) | 23.32ms |
| 결론 |  병목 가능성 있음

---

## /products/popular API 성능 이슈 요약

| 이슈 | 설명 |
|------|------|
| 정렬 인덱스 없음 | ORDER BY total_sold DESC 시 filesort 가능성 |
| SELECT * 사용 | 모든 컬럼 조회로 테이블 접근 (Index Scan 불가) |
| Join 최적화 부족 | product 테이블 조인 성능 영향 |

---


### 인덱스 추가

-- 정렬 최적화
CREATE INDEX idx_total_sold ON top_selling_product (total_sold DESC);

-- 조인 최적화
CREATE INDEX idx_product_id ON top_selling_product (product_id);



### 병목 후보
#### 1. order_item
- 주문 내역 조회 (WHERE product_id, JOIN order, JOIN product)
- 상품 통계, 최근 주문 확인 등에서 자주 조인됨
```sql
EXPLAIN
SELECT o.id AS order_id, o.created_at, oi.product_id, oi.quantity, p.name
FROM order_item oi
JOIN orders o ON oi.order_id = o.id
JOIN product p ON oi.product_id = p.id
WHERE oi.product_id = 1
ORDER BY o.created_at DESC
```
- 결과 : →type = ALL , key = Null → 인덱스 미사용
- 원인 : ORDER BY o.created_at DESC 정렬 조건이지만, orders.created_at에 인덱스 없음
- 적용 인덱스
     - CREATE INDEX idx_orders_created_at ON orders (created_at DESC);   

#### 2. orders
-  유저 주문 내역 조회 (WHERE user_id)
- 상태별 필터링 (WHERE order_status)
- 기간별 집계 가능성 (created_at)
- 적용 인덱스
   - CREATE INDEX idx_orders_user_status_date ON orders (user_id, order_status, created_at); 
  

#### 3. issued_coupon
- 유저별 발급 쿠폰 조회 (user_id)
- 적용 인덱스
    - CREATE INDEX idx_issued_coupon_user_valid ON issued_coupon (user_id, is_used, expired_at);

#### point_history
- 유저 포인트 내역 (JOIN point, WHERE point_id)
- 정렬 또는 페이징 (ORDER BY created_at DESC)
    - CREATE INDEX idx_point_history_point_created ON point_history (point_id, created_at DESC);
    






