package kr.hhplus.be.server.interfaces.api.payment.dto;

import lombok.Getter;

@Getter
public class PaymentRequest {
    private long orderId;

    public long getOrderId() {
        return orderId;
    }

}
