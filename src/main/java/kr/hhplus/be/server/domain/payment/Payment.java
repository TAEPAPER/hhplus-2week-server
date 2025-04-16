package kr.hhplus.be.server.domain.payment;

import jakarta.persistence.*;
import kr.hhplus.be.server.domain.order.Order;
import kr.hhplus.be.server.domain.order.OrderStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
public class Payment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @Enumerated(EnumType.STRING)
    @Column(length = 20, nullable = false)
    private PaymentStatus status;

    @Column(name = "paid_at")
    private LocalDateTime paidAt;

    @Builder
    private Payment(Order order, PaymentStatus status) {
        this.order = order;
        this.status = status;
    }

    public static Payment createPayment(Order order) {
        return new Payment(order, PaymentStatus.PENDING);
    }

    public void pay() {
        if (this.status == PaymentStatus.PAID) {
            throw new IllegalStateException("이미 결제된 주문입니다.");
        }

        this.status = PaymentStatus.PAID;
        this.paidAt = LocalDateTime.now();
        this.order.setOrderStatus(OrderStatus.PAID);
    }
}
