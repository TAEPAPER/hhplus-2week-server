package kr.hhplus.be.server.domain.payment;

import kr.hhplus.be.server.domain.order.Order;
import kr.hhplus.be.server.domain.order.OrderStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

class PaymentTest {

    @Mock
    private Order order;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("createPayment 메서드가 PENDING 상태의 Payment를 생성한다")
    void createPayment_정상작동() {
        // given
        when(order.getOrderStatus()).thenReturn(OrderStatus.CREATED);

        // when
        Payment payment = Payment.createPayment(order);

        // then
        assertThat(payment.getPaymentStatus()).isEqualTo(PaymentStatus.PENDING.name());
        assertThat(payment.getPaymentId()).isEqualTo(0L); // 초기값 확인
    }

    @Test
    @DisplayName("pay 메서드가 정상적으로 결제를 처리한다")
    void pay_정상작동() {
        // given
        when(order.getOrderStatus()).thenReturn(OrderStatus.CREATED);

        Payment payment = new Payment(order, PaymentStatus.PENDING);

        // when
        payment.pay(order);

        // then
        assertThat(payment.getPaymentStatus()).isEqualTo(PaymentStatus.PAID.name());
        verify(order, times(1)).setOrderStatus(OrderStatus.PAID);
    }

    @Test
    @DisplayName("pay 메서드가 이미 결제된 주문에 대해 예외를 던진다")
    void pay_이미결제된주문_예외() {
        // given
        when(order.getOrderStatus()).thenReturn(OrderStatus.PAID);

        Payment payment = new Payment(order, PaymentStatus.PENDING);

        // when & then
        assertThatThrownBy(() -> payment.pay(order))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("이미 결제된 주문입니다.");
    }
}