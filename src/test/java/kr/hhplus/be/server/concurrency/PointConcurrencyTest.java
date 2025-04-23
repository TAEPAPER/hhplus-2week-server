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
import org.springframework.transaction.annotation.Transactional;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@Transactional
@AutoConfigureMockMvc
class PointConcurrencyTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PointRepository pointRepository;

    @Autowired
    private ObjectMapper objectMapper;


    @Test
    void 동시_잔액_충전_테스트() throws InterruptedException {

        //given
        int numberOfThreads = 2;
        long userId = 1L;
        Point point = pointRepository.findByUserId(userId).get();
        long chargeAmount = 1000;

        ExecutorService executorService = Executors.newFixedThreadPool(numberOfThreads);
        CountDownLatch latch = new CountDownLatch(numberOfThreads);

        for (int i = 0; i < numberOfThreads; i++) {
            executorService.submit(() -> {
                try {

                    PointChargeRequest pointChargeRequest = new PointChargeRequest(userId, chargeAmount);

                    String json = objectMapper.writeValueAsString(pointChargeRequest);

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

      long finalBalance = pointRepository.findById(userId).get().getBalance();

        assertThat(finalBalance).isEqualTo(point.getBalance() + (numberOfThreads * chargeAmount));
    }
}