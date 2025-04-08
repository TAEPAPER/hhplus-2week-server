package kr.hhplus.be.server.interfaces.api.order.dto;

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

    public Long getOrderId() {
        return orderId;
    }

    public Long getPaymentId() {
        return paymentId;
    }

    public BigDecimal getFinalAmount() {
        return finalAmount;
    }
}