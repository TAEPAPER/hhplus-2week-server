package kr.hhplus.be.server.integration;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class PointIntegrationTest {

    @Autowired MockMvc mockMvc;

    @Test
    void 포인트_충전_성공() throws Exception {

        String jsonRequest = "{ \"userId\": 1, \"amount\": 1000 }";
        // when
        mockMvc.perform(post("/point/charge")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.balance").value(2000));
    }

    @Test
    void 포인트_충전_실패_사용자_없음() throws Exception {
        // given
        String jsonRequest = "{ \"userId\": 0, \"amount\": 1000 }";

        // when
        mockMvc.perform(post("/point/charge")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isBadRequest());

    }

    @Test
    void 포인트_충전_실패_금액_0() throws Exception {
        // given
        String jsonRequest = "{ \"userId\": 1, \"amount\": 0 }";

        // when
        mockMvc.perform(post("/point/charge")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isBadRequest());
    }

    @Test
    void 포인트_충전_실패_금액_음수() throws Exception {
        // given
        String jsonRequest = "{ \"userId\": 1, \"amount\": -1000 }";

        // when
        mockMvc.perform(post("/point/charge")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isBadRequest());
    }

}
