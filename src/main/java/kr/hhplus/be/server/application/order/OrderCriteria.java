package kr.hhplus.be.server.application.order;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class OrderCriteria {

    @Getter
    public static class OrderPayment {

        private final Long userId;
        private final Long couponId;
        private final List<OrderProduct> products;

        private OrderPayment(Long userId, Long couponId, List<OrderProduct> products) {
            this.userId = userId;
            this.couponId = couponId;
            this.products = products;
        }

        public static OrderPayment of(Long userId, Long couponId, List<OrderProduct> products) {
            return new OrderPayment(userId, couponId, products);
        }
    }

    @Getter
    public static class OrderProduct {

        private final Long productId;
        private final int quantity;

        private OrderProduct(Long productId, int quantity) {
            this.productId = productId;
            this.quantity = quantity;
        }

        public static OrderProduct of(Long productId, int quantity) {
            return new OrderProduct(productId, quantity);
        }
    }
}
