package kr.hhplus.be.server.interfaces.api.order.dto;


import kr.hhplus.be.server.domain.order.Order;

public class OrderResponse {

    private long orderId;
    private long userId;
    private String status;

    public OrderResponse(Order order) {
        this.orderId = order.getOrderId();
        this.userId = order.getUserId();
        this.status = order.getOrderStatus().name();
    }


}

