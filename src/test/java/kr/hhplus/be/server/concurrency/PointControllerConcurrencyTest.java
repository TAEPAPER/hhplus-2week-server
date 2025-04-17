package kr.hhplus.be.server.interfaces.api.point;

import kr.hhplus.be.server.application.point.PointFacade;
import kr.hhplus.be.server.application.point.repository.PointRepository;
import kr.hhplus.be.server.interfaces.api.point.dto.PointChargeRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@Transactional
class PointControllerConcurrencyTest {

    @Autowired
    private PointFacade pointFacade;

    @Autowired
    private PointRepository pointRepository;

    @Test
    void 동시_잔액_충전_테스트() throws InterruptedException {
        int threadCount = 100;
        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
        CountDownLatch latch = new CountDownLatch(threadCount);

        Long userId = 1L;
        int chargeAmount = 100;

        for (int i = 0; i < threadCount; i++) {
            executorService.execute(() -> {
                try {
                    pointFacade.charge(userId, chargeAmount);
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();

      long finalBalance = pointRepository.findById(userId).get().getBalance();

        assertThat(finalBalance).isEqualTo(threadCount * chargeAmount);
    }
}