package kr.hhplus.be.server.interfaces.payment.event;

import kr.hhplus.be.server.domain.payment.event.PaymentCompletedEvent;

public interface PaymentEventPublisher {
    void publish(PaymentCompletedEvent event);
}
