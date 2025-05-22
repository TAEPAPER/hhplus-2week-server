package kr.hhplus.be.server.domain.payment.event;

import kr.hhplus.be.server.domain.order.Order;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public class PaymentCompletedEvent {
    private final Order order;
}
