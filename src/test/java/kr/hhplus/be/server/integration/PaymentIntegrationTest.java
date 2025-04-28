package kr.hhplus.be.server.integration;


import jakarta.transaction.Transactional;
import kr.hhplus.be.server.application.order.repository.OrderRepository;
import kr.hhplus.be.server.domain.order.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;


import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class PaymentIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private OrderRepository orderRepository;


    @Test
    void 결제_성공() throws Exception {
        // given
        String jsonRequest = "{ \"orderId\": 1}";

        // when
        mockMvc.perform(post("/payments/process")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("PAID"));
        // then
        // 결제 성공 후, 주문 상태가 PAID로 변경되었는지 확인
         Order order = orderRepository.findById(1L).orElseThrow();
        assertThat(order.getOrderStatus()).isEqualTo("PAID");

    }
    @Test
    void 결제_실패_주문_없음() throws Exception {
        // given
        String jsonRequest = "{ \"orderId\": 0}";

        // when
        mockMvc.perform(post("/payments/process")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("주문정보가 없습니다!"));
    }

}
