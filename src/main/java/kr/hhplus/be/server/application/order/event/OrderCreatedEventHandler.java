package kr.hhplus.be.server.application.order.event;

import kr.hhplus.be.server.application.coupon.service.CouponService;
import kr.hhplus.be.server.application.product.service.ProductService;
import kr.hhplus.be.server.domain.coupon.IssuedCoupon;
import kr.hhplus.be.server.domain.order.event.OrderCreatedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

@Component
@RequiredArgsConstructor
@Slf4j
public class OrderCreatedEventHandler {

    private final ProductService productService;

    @Async
    @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
    public void handle(OrderCreatedEvent event) {
        try {
            // 재고 차감
            productService.deductStock(event.getQuantities());
        } catch (Exception e) {
            log.warn("[!] 주문 후처리 실패", e);
        }
    }
}
