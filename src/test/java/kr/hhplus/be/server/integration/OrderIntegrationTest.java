package kr.hhplus.be.server.integration;

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
public class OrderIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    //주문 생성
    @Test
    void 주문_생성_성공() throws Exception {
        String jsonRequest = "{ \"userId\": 1, \"orderItems\": [{ \"productId\": 1, \"quantity\": 2 }], \"couponId\": 1 }";

        mockMvc.perform(post("/orders/order")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.userId").value(1));
    }
    //주문 생성 실패 - 사용자 없음
    @Test
    void 주문_생성_실패_사용자_없음() throws Exception {
        String jsonRequest = "{ \"userId\": 0, \"orderItems\": [{ \"productId\": 1, \"quantity\": 2 }], \"couponId\": 1 }";

        mockMvc.perform(post("/orders/order")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("사용자 정보가 없습니다!"));
    }
    //주문 생성 실패 - 상품 없음
    @Test
    void 주문_생성_실패_상품_없음() throws Exception {
        String jsonRequest = "{ \"userId\": 1, \"orderItems\": [{ \"productId\": 0, \"quantity\": 2 }], \"couponId\": 1 }";

        mockMvc.perform(post("/orders/order")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("상품 정보가 없습니다!"));
    }

}
