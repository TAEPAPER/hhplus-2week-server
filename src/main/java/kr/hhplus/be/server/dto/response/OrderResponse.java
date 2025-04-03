package kr.hhplus.be.server.dto.response;

import java.math.BigDecimal;

public class OrderResponse {
    private Long orderId;
    private Long paymentId;
    private BigDecimal finalAmount;

    public OrderResponse(Long orderId,Long paymentId, BigDecimal finalAmount) {
        this.orderId = orderId;
        this.paymentId = paymentId;
        this.finalAmount = finalAmount;
    }
}