package kr.hhplus.be.server.integration;

import kr.hhplus.be.server.application.point.repository.PointRepository;
import kr.hhplus.be.server.domain.point.Point;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class PointIntegrationTest {

    @Autowired MockMvc mockMvc;

    @Autowired
    PointRepository pointRepository;

    @Test
    void 포인트_충전_성공() throws Exception {

        //given
        Point point = pointRepository.findByUserId(1).get();
        String jsonRequest = "{ \"userId\": 1, \"amount\": 1000 }";

        // when
        mockMvc.perform(post("/point/charge")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(jsonRequest))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.balance").value(point.getBalance() + 1000));
        // then
        Point updatedPoint = pointRepository.findByUserId(1).get();
        assertThat(updatedPoint.getBalance()).isEqualTo(point.getBalance() + 1000);
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
        // then
        // 예외 처리 로직이 없으므로, DB에 포인트가 생성되지 않아야 함
        Point point = pointRepository.findByUserId(0).orElse(null);
        assertThat(point).isNull();

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
        // then
        // 예외 처리 로직이 없으므로, DB에 포인트가 생성되지 않아야 함
        Point point = pointRepository.findByUserId(1).orElse(null);
        assertThat(point).isNotNull();

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
        // then
        // 예외 처리 로직이 없으므로, DB에 포인트가 생성되지 않아야 함
        Point point = pointRepository.findByUserId(1).orElse(null);
        assertThat(point).isNotNull();
    }

}
