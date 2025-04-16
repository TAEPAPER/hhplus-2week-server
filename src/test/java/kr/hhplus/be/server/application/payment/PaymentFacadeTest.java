
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
        // given
        long orderId = 1L;
        long userId = 100L;
        Order order = mock(Order.class);
        Point point = mock(Point.class);
        User user = mock(User.class);


        Payment payment = mock(Payment.class);
        List<Order.ProductQuantity> productQuantities = List.of(
                new Order.ProductQuantity(mock(Product.class), 2),
                new Order.ProductQuantity(mock(Product.class), 1)
        );
        when(user.getId()).thenReturn(userId);
        when(order.getUser()).thenReturn(user);
        when(order.getId()).thenReturn(orderId);
        when(order.getUser()).thenReturn(user);

        when(orderService.getOrderById(orderId)).thenReturn(order);
        when(productService.getProductsStock(Map.of(1L, 2, 2L, 1))).thenReturn(productQuantities);
        doNothing().when(productService).validateStockAvailability(productQuantities);
        when(pointService.getPointByUserId(userId)).thenReturn(point);
        when(paymentService.processPayment(order, point)).thenReturn(payment);
        when(order.getTotalPrice()).thenReturn(3000L); // 총 가격 설정


        // when
        Payment result = paymentFacade.processPayment(orderId);

        // then
        assertThat(result).isEqualTo(payment);
        verify(orderService, times(1)).getOrderById(orderId);
        verify(productService, times(1)).getProductsStock(Map.of(1L, 2, 2L, 1));
        verify(productService, times(1)).validateStockAvailability(productQuantities);
        verify(pointService, times(1)).getPointByUserId(userId);
        verify(paymentService, times(1)).processPayment(order, point);
        verify(pointHistoryService, times(1)).recordUse(userId, order.getTotalPrice());
        verify(productService, times(1)).deductStock(productQuantities);
    }
}
