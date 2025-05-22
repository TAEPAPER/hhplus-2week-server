package kr.hhplus.be.server.application.payment.event;

import kr.hhplus.be.server.domain.payment.event.PaymentCompletedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionalEventListener;
import org.springframework.transaction.event.TransactionPhase;

@Component
@RequiredArgsConstructor
@Slf4j
public class PaymentCompletedEventHandler {

    private final DataPlatformSender dataPlatformSender;

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handle(PaymentCompletedEvent event) {
        try {
            dataPlatformSender.send(event.getOrder());
        } catch (Exception e) {
            log.warn("[!] 외부 플랫폼 전송 실패", e);
        }
    }
}
