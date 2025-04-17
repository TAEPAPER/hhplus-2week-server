package kr.hhplus.be.server.concurrency;

import kr.hhplus.be.server.application.coupon.repository.IssuedCouponRepository;
import kr.hhplus.be.server.application.coupon.service.CouponService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
class CouponControllerConcurrencyTest {

    @Autowired
    private CouponService couponService;

    @Autowired
    private IssuedCouponRepository issuedCouponRepository;

    @Test
    void 동시_쿠폰_발급_테스트() throws InterruptedException {
        int threadCount = 200;
        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
        CountDownLatch latch = new CountDownLatch(threadCount);

        Long couponId = 1L;
        Long userId = 1L;

        for (int i = 0; i < threadCount; i++) {
            executorService.execute(() -> {
                try {
                    couponService.issueCoupon(userId, couponId);
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();

        long issuedCount = issuedCouponRepository.countByCouponId(couponId);
        assertThat(issuedCount).isEqualTo(1);
    }
}