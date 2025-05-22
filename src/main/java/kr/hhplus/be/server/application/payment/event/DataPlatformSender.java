package kr.hhplus.be.server.application.payment.event;

import kr.hhplus.be.server.domain.order.Order;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class DataPlatformSender {

    public void send(Order order) {
        log.info("외부 데이터 플랫폼으로 주문 정보 전송: 주문번호 {}", order.getId());
    }
}
