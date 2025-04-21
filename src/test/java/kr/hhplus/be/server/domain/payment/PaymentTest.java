package kr.hhplus.be.server.domain.payment;

import kr.hhplus.be.server.domain.order.Order;
import kr.hhplus.be.server.domain.order.OrderStatus;
import kr.hhplus.be.server.domain.user.User;
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
        assertThat(payment.getStatus().name()).isEqualTo(PaymentStatus.PENDING.name());
        assertThat(payment.getId()).isEqualTo(0L); // 초기값 확인
    }

    @Test
    @DisplayName("pay 메서드가 정상적으로 결제를 처리한다")
    void pay_정상작동() {
        // given
        User user = User.builder().id(1L).name("Test User").build();
        Order order = Order.builder()
                        .user(user)
                        .orderStatus(OrderStatus.CREATED)
                        .build();
        when(order.getOrderStatus()).thenReturn(OrderStatus.CREATED);

        Payment payment = Payment.builder().order(order).status(PaymentStatus.PENDING).build();
        // when
        payment.pay();

        // then
        assertThat(payment.getStatus().name()).isEqualTo(PaymentStatus.PAID.name());
        assertThat(payment.getOrder().getOrderStatus()).isEqualTo(OrderStatus.PAID);
        verify(order, times(1)).setOrderStatus(OrderStatus.PAID);
    }

    @Test
    @DisplayName("pay 메서드가 이미 결제된 주문에 대해 예외를 던진다")
    void pay_이미결제된주문_예외() {
        // given
        User user = User.builder().id(1L).name("Test User").build();
        Order order = Order.builder()
                .user(user)
                .orderStatus(OrderStatus.CREATED)
                .build();

        when(order.getOrderStatus()).thenReturn(OrderStatus.PAID);

        Payment payment = Payment.builder().order(order).status(PaymentStatus.PAID).build();

        // when & then
        assertThatThrownBy(() -> payment.pay())
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("이미 결제된 주문입니다.");
    }
}