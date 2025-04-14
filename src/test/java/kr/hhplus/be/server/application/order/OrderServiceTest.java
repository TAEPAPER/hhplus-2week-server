package kr.hhplus.be.server.application.order;

import kr.hhplus.be.server.application.order.repository.OrderRepository;
import kr.hhplus.be.server.application.order.service.OrderService;
import kr.hhplus.be.server.domain.coupon.IssuedCoupon;
import kr.hhplus.be.server.domain.order.Order;
import kr.hhplus.be.server.domain.product.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class OrderServiceTest {

    @Mock
    private OrderRepository orderRepository;

    @InjectMocks
    private OrderService orderService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void 주문_생성_성공() {
        // Given
        long userId = 1L;
        IssuedCoupon coupon = mock(IssuedCoupon.class);
        Product product1 = mock(Product.class);
        Product product2 = mock(Product.class);
        List<Order.ProductQuantity> quantities = List.of(
                new Order.ProductQuantity(product1, 2),
                new Order.ProductQuantity(product2, 1)
        );
        Order order = mock(Order.class);

        when(orderRepository.save(any(Order.class))).thenReturn(order);

        // When
        Order result = orderService.placeOrder(userId, quantities, coupon);

        // Then
        assertNotNull(result);
        verify(orderRepository, times(1)).save(any(Order.class));
    }

    @Test
    void 주문_조회_성공() {
        // Given
        long orderId = 1L;
        Order order = mock(Order.class);

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));

        // When
        Order result = orderService.getOrderById(orderId);

        // Then
        assertNotNull(result);
        assertEquals(order, result);
        verify(orderRepository, times(1)).findById(orderId);
    }

    @Test
    void 주문정보_없으면_에러를_뱉는다() {
        // Given
        long orderId = 1L;

        when(orderRepository.findById(orderId)).thenReturn(Optional.empty());

        // When & Then
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> orderService.getOrderById(orderId));
        assertEquals("주문정보가 없습니다!", exception.getMessage());
    }
}