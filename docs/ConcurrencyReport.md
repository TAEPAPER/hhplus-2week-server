#  동시성 이슈 분석 및 해결 보고서

## 동시성 이슈 분석 대상

1. 상품 재고 차감 및 복원
2. 사용자 포인트 충전
3. 선착순 쿠폰 발급
---

## 1. 상품 재고 차감 및 복원

### 동시성 이슈

- 다수 사용자가 동시에 같은 상품을 구매할 경우, 재고 수량이 음수가 되는 문제가 발생할 수 있음.
- 예: 재고가 1인데 두 사용자가 동시에 구매 → 둘 다 성공 → 재고 -1

### 해결 전략

- `@Lock(LockModeType.PESSIMISTIC_WRITE)` 사용
- 트랜잭션 시작 시 재고 행을 배타적 잠금하여 동시에 접근 불가하도록 설정

### WHY 비관적락?
- 재고 차감은 선착순 처리가 요구되는 상황이고, 데이터 정확성이 매우 중요함
- 재고는 한정되어 있고, 동시에 많은 사용자가 접근할 수 있는 상황에서는 낙관적 락 사용시 충돌 빈도가 높아질 가능성이 있다고 판단함
- 정확성이 중요한 상황이므로 비관적 락이 더 적합하다고 생각함

### 적용 코드

```java
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT p FROM Product p JOIN FETCH p.inventory WHERE p.id = :id")
    Product findByIdWithLock(long id);
```    
### 테스트 코드
```
@Test
    void 동시_주문_시_재고_테스트() throws InterruptedException, JsonProcessingException {

        //given
        int numberOfThreads = 2;

        long productId = 1L;
        Product product = productRepository.findById(1L);
        int initialInventory = product.getInventory().getStock();

        ExecutorService executorService = Executors.newFixedThreadPool(numberOfThreads);
        CountDownLatch latch = new CountDownLatch(numberOfThreads);


        for (int i = 0; i < numberOfThreads; i++) {
            executorService.submit(() -> {
                try {

                    OrderRequest orderRequest = OrderRequest.of(1L,0L, List.of(
                            new OrderRequest.OrderItemRequest(productId, 1)
                    ));

                    String json = objectMapper.writeValueAsString(orderRequest);

                    mockMvc.perform(post("/orders/order")
                                    .contentType("application/json")
                                    .content(json))
                            .andExpect(status().isOk());
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await(); // 모든 스레드가 작업을 완료할 때까지 대기
        executorService.shutdown();

        //when
        //주문 후 재고 확인
        Product finalProduct = productRepository.findById(1L);

       int finalInventory = finalProduct.getInventory().getStock();
        //then
        //재고 확인
        assertEquals(initialInventory - numberOfThreads, finalInventory);
    }

```
## 2. 유저의 포인트 충전
### 동시성 이슈
- 포인트를 동시에 충전하거 사용할 경우 포인트 중복 증가/차감 현상 발생 가능

### 해결 전략
- 유저의 포인트 테이블 조회 시 비관적 락 적용

### WHY 비관적락?
- 돈과 관련된 것은 정합성이 중요
- 락 비용보다 정합성 확보가 더 중요하므로 비관적 락을 사용

### 적용 코드
```java
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT p FROM Point p JOIN FETCH p.user u WHERE p.user.id = :userId")
    Optional<Point> findByUserIdWithLock(long userId);
```    
### 테스트 코드
```java
 @Test
    void 동시_포인트_충전_테스트() throws Exception {
        // given
        int numberOfThreads = 2;
        long userId = 1L;
        long chargeAmount = 1000L;

        Point point = pointRepository.findByUserId(userId).orElseThrow();
        // 초기 잔액
        long initialBalance = point.getBalance();

        ExecutorService executorService = Executors.newFixedThreadPool(numberOfThreads);
        CountDownLatch latch = new CountDownLatch(numberOfThreads);

        for (int i = 0; i < numberOfThreads; i++) {
            executorService.submit(() -> {
                try {

                    PointChargeRequest request = new PointChargeRequest(userId, chargeAmount);
                    String json = objectMapper.writeValueAsString(request);

                    mockMvc.perform(post("/point/charge")
                                    .contentType("application/json")
                                    .content(json))
                            .andExpect(status().isOk());

                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();
        executorService.shutdown();

        // then
        Point result = pointRepository.findByUserId(userId).orElseThrow();
        long expected = initialBalance + (numberOfThreads * chargeAmount);
        long actual = result.getBalance();

        assertEquals(expected, actual);
    }
```  

## 3. 선착순 쿠폰 발급
### 동시성 이슈
- 선착순으로 발급해야할 쿠폰이 동시 접근으로 인해 중복 발급되는 경우 발생할 수 있음
- 예 : 쿠폰 수량이 1인데 동시에 여러 명이 발급 시도

### 해결 전략
- 쿠폰 발급 요청 시, 쿠폰 테이블에서 해당 쿠폰을 @Lock(LockModeType.PESSIMISTIC_WRITE)으로 조회
- 쿠폰의 수량(total_quantity)을 감소시키기 전에 DB 수준에서 해당 행을 잠금 처리하여, 동시 접근을 방지
- 동시에 여러 사용자가 같은 쿠폰을 발급하려고 할 경우, 락이 해제될 때까지 대기

### WHY 비관적락?
-  선착순 발급은 시스템이 정확하게 N명에게만 발급해야 하므로, 중복 발급이 절대 허용되지 않음
-  비관적 락은 쿠폰 행을 먼저 잠금 처리하고 이후 로직을 진행하므로, 중복 발급 없이 순차적 처리가 가능
-  트래픽이 몰리는 쿠폰 발급 시점에도 정합성을 보장할 수 있어, 비관적 락을 선택함

### 적용 코드
```java
    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT c FROM Coupon c WHERE c.id = :id")
    Optional<Coupon> findByIdWithLock(Long id);
```  
### 테스트 코드
```java
   @Test
    void 동시_쿠폰_발급_테스트() throws InterruptedException {
        //given

        int numberOfThreads = 2;
        Long couponId = 2L;
        Long userId = 1L;

        int initialCouponQuantity = couponRepository.findTotalQuantityById(couponId);

        ExecutorService executorService = Executors.newFixedThreadPool(numberOfThreads);
        CountDownLatch latch = new CountDownLatch(numberOfThreads);

        for (int i = 0; i < numberOfThreads; i++) {
            executorService.execute(() -> {
                try {
                    CouponIssueRequest request = new CouponIssueRequest(userId, couponId);
                    String json = objectMapper.writeValueAsString(request);

                    mockMvc.perform(post("/coupons/issue")
                                    .contentType("application/json")
                                    .content(json))
                            .andExpect(status().isOk());

                } catch (Exception e) {
                    e.printStackTrace();
                    throw new RuntimeException("스레드 내 요청 실패", e);
                } finally {
                    latch.countDown();
                }
            });
        }


        latch.await();
        executorService.shutdown();

        //then
        int issuedCount = couponRepository.findTotalQuantityById(couponId);
        assertThat(issuedCount).isEqualTo(initialCouponQuantity - numberOfThreads);
    }
```  

