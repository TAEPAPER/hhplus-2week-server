package kr.hhplus.be.server.interfaces.api.order.dto;


import kr.hhplus.be.server.domain.order.Order;
import lombok.Getter;

@Getter
public class OrderResponse {

    private long orderId;
    private long userId;
    private String status;

    public OrderResponse(Order order) {
        this.orderId = order.getId();
        this.userId = order.getUser().getId();
        this.status = order.getOrderStatus().name();
    }


}

