package kr.hhplus.be.server.domain.order.event;

import kr.hhplus.be.server.domain.order.Order;
import kr.hhplus.be.server.domain.order.Order.ProductQuantity;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.List;

@Getter
@RequiredArgsConstructor
public class OrderCreatedEvent {
    private final Order order;
    private final List<ProductQuantity> quantities;
    private final Long couponId;
}
