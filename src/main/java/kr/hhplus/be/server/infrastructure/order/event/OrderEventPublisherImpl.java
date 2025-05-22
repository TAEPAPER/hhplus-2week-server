package kr.hhplus.be.server.infrastructure.order.event;

import kr.hhplus.be.server.domain.order.event.OrderCreatedEvent;
import kr.hhplus.be.server.interfaces.order.event.OrderEventPublisher;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OrderEventPublisherImpl implements OrderEventPublisher {
    private final ApplicationEventPublisher applicationEventPublisher;

    @Override
    public void publish(OrderCreatedEvent event) {
        applicationEventPublisher.publishEvent(event);
    }
}
