package kr.hhplus.be.server.concurrency;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import kr.hhplus.be.server.application.product.repository.ProductRepository;
import kr.hhplus.be.server.domain.product.Product;
import kr.hhplus.be.server.interfaces.order.OrderRequest;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.mock;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class OrderConcurrencyTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private ProductRepository productRepository;

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
}