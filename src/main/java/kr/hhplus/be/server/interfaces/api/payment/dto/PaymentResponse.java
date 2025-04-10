package kr.hhplus.be.server.interfaces.api.payment.dto;

import kr.hhplus.be.server.domain.payment.Payment;

public class PaymentResponse {

    private Long paymentId;
    private String status; // 결제 상태 (예: SUCCESS, FAILED 등)

    public PaymentResponse(Payment payment) {
        this.paymentId = payment.getPaymentId();
        this.status = payment.getPaymentStatus();
    }
}