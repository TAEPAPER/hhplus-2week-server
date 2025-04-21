package kr.hhplus.be.server.concurrency;

import kr.hhplus.be.server.application.product.service.ProductService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@SpringBootTest
class ProductControllerConcurrencyTest {

    @Autowired
    private ProductService productService;

    @Test
    void 동시_상품_조회_테스트() throws InterruptedException {
        int threadCount = 50;
        ExecutorService executorService = Executors.newFixedThreadPool(threadCount);
        CountDownLatch latch = new CountDownLatch(threadCount);

        for (int i = 0; i < threadCount; i++) {
            executorService.execute(() -> {
                try {
                    productService.getAll();
                } finally {
                    latch.countDown();
                }
            });
        }

        latch.await();

        assertThat(true).isTrue();

    }
}