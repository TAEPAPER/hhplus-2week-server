
package kr.hhplus.be.server.application.payment;

import kr.hhplus.be.server.application.order.service.OrderService;
import kr.hhplus.be.server.application.payment.service.PaymentService;
import kr.hhplus.be.server.application.point.service.PointService;
import kr.hhplus.be.server.application.pointHistory.service.PointHistoryService;
import kr.hhplus.be.server.application.product.service.ProductService;
import kr.hhplus.be.server.domain.order.Order;
import kr.hhplus.be.server.domain.order.OrderItem;
import kr.hhplus.be.server.domain.payment.Payment;
import kr.hhplus.be.server.domain.point.Point;
import kr.hhplus.be.server.domain.product.Product;
import kr.hhplus.be.server.domain.user.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class PaymentFacadeTest {

    @Mock
    private PaymentService paymentService;

    @Mock
    private OrderService orderService;

    @Mock
    private PointService pointService;

    @Mock
    private PointHistoryService pointHistoryService;

    @Mock
    private ProductService productService;

    @InjectMocks
    private PaymentFacade paymentFacade;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    void 결제_처리_성공() {
        //given
        long orderId = 1L;
        Order order = mock(Order.class);
        User user = mock(User.class);
        Point point = mock(Point.class);
        Payment payment = mock(Payment.class);

        when(orderService.getOrderById(orderId)).thenReturn(order);
        when(order.getUser()).thenReturn(user);
        when(pointService.getPointByUserId(user.getId())).thenReturn(point);
        when(paymentService.processPayment(order, point)).thenReturn(payment);

        //when
        Payment result = paymentFacade.processPayment(orderId);

        //then
        assertThat(result).isEqualTo(payment);
        verify(pointHistoryService).recordUse(user.getId(), order.getTotalPrice());
    }
}
