package kr.hhplus.be.server.domain.payment;

import kr.hhplus.be.server.domain.order.Order;
import kr.hhplus.be.server.domain.order.OrderStatus;

public class Payment {

    private long paymentId;
    private Order order;
    private PaymentStatus paymentStatus;


    public Payment(Order order, PaymentStatus paymentStatus) {
        this.order = order;
        this.paymentStatus = paymentStatus;
    }

    public String getPaymentStatus() {
        return paymentStatus.name();
    }

    public long getPaymentId() {
        return paymentId;
    }

    public Payment pay(Order order) {
        if (order.getOrderStatus().name().equals(PaymentStatus.PAID.name())){
            throw new IllegalStateException("이미 결제된 주문입니다.");
        }
        //order의 상태 변경
        this.order.setOrderStatus(OrderStatus.PAID);
        //결제 상태 변경
        this.paymentStatus = PaymentStatus.PAID;
        return this;
    }

    public static Payment createPayment(Order order) {
        // 결제 생성 로직
        return new Payment(order, PaymentStatus.PENDING);
    }

}
