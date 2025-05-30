package kr.hhplus.be.server.infrastructure.kafka;

import kr.hhplus.be.server.domain.payment.event.PaymentCompletedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class PaymentCompletedProducer {

    private final KafkaTemplate<String, PaymentCompletedEvent> kafkaTemplate;

    private final String topic = "payment.completed";

    public void send(PaymentCompletedEvent event) {
        log.info("Kafka 전송: {}", event);
        kafkaTemplate.send(topic, event);
    }
}
