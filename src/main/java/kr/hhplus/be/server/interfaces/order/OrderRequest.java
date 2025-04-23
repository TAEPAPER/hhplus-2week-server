package kr.hhplus.be.server.interfaces.order;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import kr.hhplus.be.server.application.order.OrderCriteria;
import lombok.Getter;

import java.util.List;

@Getter
public class OrderRequest {

    @NotNull(message = "사용자 ID는 필수 입니다.")
    private Long userId;
    private Long couponId;

    @NotEmpty(message = "상품 목록은 1개 이상이여야 합니다.")
    private List<OrderItemRequest> orderItems;

    private OrderRequest(Long userId, Long couponId, List<OrderItemRequest> orderItems) {
        this.userId = userId;
        this.orderItems = orderItems;
        this.couponId = couponId;
    }

    public static OrderRequest of(Long userId, Long couponId, List<OrderItemRequest> orderItems) {
        return new OrderRequest(userId, couponId, orderItems);
    }

    public OrderCriteria.OrderPayment toCriteria() {
        return OrderCriteria.OrderPayment.of(userId, couponId, orderItems.stream()
                .map(OrderItemRequest::toCriteria)
                .toList());
    }

    @Getter
    public static class OrderItemRequest {

        @NotNull(message = "상품 ID는 필수입니다.")
        private long productId;

        @NotNull(message = "상품 구매 수량은 필수입니다.")
        @Positive(message = "상품 구매 수량은 양수여야 합니다.")
        private int quantity;

        public OrderItemRequest(long productId, int quantity) {
            this.productId = productId;
            this.quantity = quantity;
        }

        public long getProductId() {
            return productId;
        }

        public int getQuantity() {
            return quantity;
        }

        public OrderCriteria.OrderProduct toCriteria() {
            return OrderCriteria.OrderProduct.of(productId, quantity);
        }
    }

}

