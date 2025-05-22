package kr.hhplus.be.server.infrastructure.payment.event;
import kr.hhplus.be.server.domain.payment.event.PaymentCompletedEvent;
import kr.hhplus.be.server.interfaces.payment.event.PaymentEventPublisher;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PaymentEventPublisherImpl implements PaymentEventPublisher {

    private final ApplicationEventPublisher applicationEventPublisher;

    @Override
    public void publish(PaymentCompletedEvent event) {
        applicationEventPublisher.publishEvent(event);
    }
}
