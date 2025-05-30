package kr.hhplus.be.server.infrastructure.kafka;

import kr.hhplus.be.server.application.payment.event.DataPlatformSender;
import kr.hhplus.be.server.domain.payment.event.PaymentCompletedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class PaymentCompletedConsumer {

    private final DataPlatformSender dataPlatformSender;

    @KafkaListener(topics = "payment.completed", groupId = "payment-group")
    public void listen(PaymentCompletedEvent event) {
        log.info("Kafka 메시지 수신: {}", event.getOrder());
        dataPlatformSender.send(event.getOrder());
    }
}
