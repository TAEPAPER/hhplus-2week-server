package kr.hhplus.be.server.concurrency;

import jakarta.persistence.EntityManager;
import kr.hhplus.be.server.application.coupon.repository.CouponRepository;
import kr.hhplus.be.server.application.coupon.repository.IssuedCouponRepository;
import kr.hhplus.be.server.application.coupon.service.CouponService;
import kr.hhplus.be.server.domain.coupon.Coupon;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;


@SpringBootTest
@AutoConfigureMockMvc
class CouponConcurrencyTest {

    @Autowired
    private CouponService couponService;

    @Autowired
    private IssuedCouponRepository issuedCouponRepository;

    @Autowired
    private CouponRepository couponRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    EntityManager em;

    @Autowired
    private TestDataInitializer testDataInitializer;

    private Coupon testCoupon;

    @BeforeEach
    void setUp() {
        testCoupon = testDataInitializer.initCoupon();  // 트랜잭션 커밋된 상태
    }


    @Test
    void 동시_쿠폰_발급_테스트() throws InterruptedException {
        //given
        int numberOfThreads = 1;
        Long userId = 1L;

        int initialCouponQuantity = couponRepository.findTotalQuantityById(testCoupon.getId());

        ExecutorService executorService = Executors.newFixedThreadPool(numberOfThreads);
        CountDownLatch latch = new CountDownLatch(numberOfThreads);

        long[] userIds = {1L, 2L, 3L, 4L, 5L};

        for (int i = 0; i < numberOfThreads; i++) {
            int finalI = i;
            executorService.submit(() -> {
                try {
                    couponService.issueCoupon(userIds[finalI], testCoupon.getId());

                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();
        executorService.shutdown();

        em.clear();
        //then
        int issuedCount = couponRepository.findTotalQuantityById(testCoupon.getId());
        assertThat(issuedCount).isEqualTo(initialCouponQuantity - numberOfThreads);
    }


    @Test
    void couponRepository_작동_테스트(){
       int count =  couponRepository.findTotalQuantityById(testCoupon.getId());
         assertThat(count).isEqualTo(500);
    }



    @Test
    void findTotalQuantityById_성공() {

        int quantity = couponRepository.findTotalQuantityById(testCoupon.getId());
        assertThat(quantity).isEqualTo(500);
    }


    @Test
    @Transactional
    void Lock_test(){

        Coupon result = couponRepository.findByIdWithLock(testCoupon.getId())
                .orElseThrow(() -> new IllegalArgumentException("쿠폰이 존재하지 않습니다."));

        assertThat(result.getId()).isEqualTo(testCoupon.getId());



    }
}