package kr.hhplus.be.server.interfaces.api.order.dto;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public class OrderRequest {

    private long userId;
    private List<OrderItemRequest> orderItems;
    private long couponId; // Optional 사용

    public OrderRequest(long userId, List<OrderItemRequest> orderItems, long couponId) {
        this.userId = userId;
        this.orderItems = orderItems;
        this.couponId = couponId;
    }

    public List<OrderItemRequest> getOrderItems() {
        return orderItems;
    }

    public long getUserId() {
        return userId;
    }

    public long getCouponId() {
        return couponId;
    }
}
