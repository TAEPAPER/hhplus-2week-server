package kr.hhplus.be.server.concurrency;

import com.fasterxml.jackson.databind.ObjectMapper;
import kr.hhplus.be.server.application.point.repository.PointRepository;
import kr.hhplus.be.server.domain.point.Point;
import kr.hhplus.be.server.interfaces.point.dto.PointChargeRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class PointConcurrencyTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PointRepository pointRepository;

    @Autowired
    private ObjectMapper objectMapper;

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
}
