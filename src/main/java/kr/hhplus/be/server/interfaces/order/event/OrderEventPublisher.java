package kr.hhplus.be.server.interfaces.order.event;

import kr.hhplus.be.server.domain.order.event.OrderCreatedEvent;

public interface OrderEventPublisher {
    void publish(OrderCreatedEvent event);
}
