package kr.hhplus.be.server.interfaces.api.payment.dto;

import kr.hhplus.be.server.domain.payment.Payment;
import lombok.Getter;

@Getter
public class PaymentResponse {

    private Long paymentId;
    private String status; // 결제 상태 (예: SUCCESS, FAILED 등)

    public PaymentResponse(Payment payment) {
        this.paymentId = payment.getId();
        this.status = payment.getStatus().name();
    }
}