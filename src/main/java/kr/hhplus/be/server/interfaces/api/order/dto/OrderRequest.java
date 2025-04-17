package kr.hhplus.be.server.interfaces.api.order.dto;

import jakarta.annotation.Nullable;
import lombok.Getter;

import java.util.List;

@Getter
public class OrderRequest {

    private long userId;
    private List<OrderItemRequest> orderItems;
    private Long couponId;  // ✅ long → Long

    public OrderRequest() {}

    public OrderRequest(long userId, List<OrderItemRequest> orderItems, Long couponId) {
        this.userId = userId;
        this.orderItems = orderItems;
        this.couponId = couponId;
    }


}

