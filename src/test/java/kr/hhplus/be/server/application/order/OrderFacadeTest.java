package kr.hhplus.be.server.application.order;

import kr.hhplus.be.server.application.coupon.service.CouponService;
import kr.hhplus.be.server.application.order.service.OrderService;
import kr.hhplus.be.server.application.product.service.ProductService;
import kr.hhplus.be.server.domain.coupon.IssuedCoupon;
import kr.hhplus.be.server.domain.coupon.NoCoupon;
import kr.hhplus.be.server.domain.order.Order;
import kr.hhplus.be.server.domain.product.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class OrderFacadeTest {

    @Mock
    private OrderService orderService;

    @Mock
    private ProductService productService;

    @Mock
    private CouponService couponService;

    @InjectMocks
    private OrderFacade orderFacade;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void 쿠폰이_있는_주문_생성_성공() {
        // given
        long userId = 1L;
        long couponId = 100L;
        Map<Long, Integer> productQuantities = Map.of(1L, 2, 2L, 1);

        IssuedCoupon coupon = mock(IssuedCoupon.class);
        List<Order.ProductQuantity> quantities = List.of(
                new Order.ProductQuantity(mock(Product.class), 2),
                new Order.ProductQuantity(mock(Product.class), 1)
        );
        Order order = mock(Order.class);

        when(productService.getProductsStock(productQuantities)).thenReturn(quantities);
        when(couponService.getById(couponId)).thenReturn(coupon);
        when(orderService.placeOrder(userId, quantities, coupon)).thenReturn(order);

        // when
        Order result = orderFacade.order(userId, productQuantities, couponId);

        // then
        assertThat(result).isEqualTo(order);
        verify(productService, times(1)).getProductsStock(productQuantities);
        verify(couponService, times(1)).getById(couponId);
        verify(orderService, times(1)).placeOrder(userId, quantities, coupon);
    }

    @Test
    void 쿠폰이_없는_주문_생성_성공() {
        // given
        long userId = 1L;
        long couponId = 0L; // 쿠폰 없음
        Map<Long, Integer> productQuantities = Map.of(1L, 2, 2L, 1);

        List<Order.ProductQuantity> quantities = List.of(
                new Order.ProductQuantity(mock(Product.class), 2),
                new Order.ProductQuantity(mock(Product.class), 1)
        );
        Order order = mock(Order.class);

        when(productService.getProductsStock(productQuantities)).thenReturn(quantities);
        when(orderService.placeOrder(eq(userId), eq(quantities), any(NoCoupon.class))).thenReturn(order);

        // when
        Order result = orderFacade.order(userId, productQuantities, couponId);

        // then
        assertThat(result).isEqualTo(order);
        verify(productService, times(1)).getProductsStock(productQuantities);
        verify(couponService, never()).getById(anyLong());
        verify(orderService, times(1)).placeOrder(eq(userId), eq(quantities), any(NoCoupon.class));
    }
}