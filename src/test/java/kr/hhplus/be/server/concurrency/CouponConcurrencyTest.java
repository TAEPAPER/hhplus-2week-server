package kr.hhplus.be.server.concurrency;

import com.fasterxml.jackson.core.JsonProcessingException;
import kr.hhplus.be.server.application.coupon.repository.CouponRepository;
import kr.hhplus.be.server.application.coupon.repository.IssuedCouponRepository;
import kr.hhplus.be.server.application.coupon.service.CouponService;
import kr.hhplus.be.server.interfaces.coupon.dto.CouponIssueRequest;
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
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.mockito.Mockito.mock;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
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
}