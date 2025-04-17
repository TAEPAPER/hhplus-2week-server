package kr.hhplus.be.server.integration.payment;


import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;


import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class PaymentIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

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
