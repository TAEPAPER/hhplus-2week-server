package kr.hhplus.be.server.interfaces.api.order.dto;

import java.util.List;

public class OrderRequest {

    private long userId;
    private List<OrderItemRequest> orderItems;

    public OrderRequest(long userId, List<OrderItemRequest> orderItems)
    {
        this.userId = userId;
        this.orderItems = orderItems;
    }

    public List<OrderItemRequest> getOrderItems() {
        return orderItems;
    }

    public long getUserId() {
        return userId;
    }
}
