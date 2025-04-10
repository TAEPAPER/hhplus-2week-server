package kr.hhplus.be.server.interfaces.api.order.dto;

import jakarta.annotation.Nullable;

import java.util.List;


public class OrderRequest {

    private long userId;
    private List<OrderItemRequest> orderItems;
    @Nullable
    private long couponId;

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
