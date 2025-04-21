package kr.hhplus.be.server.domain.order;

import kr.hhplus.be.server.domain.coupon.IssuedCoupon;
import kr.hhplus.be.server.domain.product.Product;
import kr.hhplus.be.server.domain.user.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

class OrderTest {

    @Mock
    private Product product;

    @Mock
    private IssuedCoupon coupon;

    @Test
    @DisplayName("Order 생성 테스트 - 정상적인 경우")
    void createOrder_정상작동() {
        // given
        MockitoAnnotations.openMocks(this);
        long userId = 1L;
        int quantity = 2;
        long productPrice = 5000L;
        long discount = 2000L;

        User user = User.builder().id(userId).name("Test User").build();


        when(product.getId()).thenReturn(1L);
        when(product.getPrice()).thenReturn(productPrice);
        doNothing().when(product).isStockAvailable(quantity);

        when(coupon.isValid()).thenReturn(true);
        when(coupon.calculateDiscount(productPrice * quantity)).thenReturn(discount);

        List<Order.ProductQuantity> productQuantities = List.of(new Order.ProductQuantity(product, quantity));

        // when
        Order order = Order.create(user, productQuantities, coupon);

        // then
        assertThat(order.getUser().getId()).isEqualTo(userId);
        assertThat(order.getOrderStatus()).isEqualTo(OrderStatus.CREATED);
        assertThat(order.getTotalPrice()).isEqualTo((productPrice * quantity) - discount);
        assertThat(order.getOrderItems()).hasSize(1);
        verify(product, times(1)).isStockAvailable(quantity);
        verify(coupon, times(1)).isValid();
        verify(coupon, times(1)).calculateDiscount(productPrice * quantity);
    }

    @Test
    @DisplayName("Order 생성 테스트 - 재고 부족 예외")
    void createOrder_재고부족_예외() {
        // given
        MockitoAnnotations.openMocks(this);
        long userId = 1L;
        int quantity = 2;
        User user = User.builder().id(userId).name("Test User").build();

        doThrow(new IllegalStateException("상품 재고 부족")).when(product).isStockAvailable(quantity);

        List<Order.ProductQuantity> productQuantities = List.of(new Order.ProductQuantity(product, quantity));

        // when & then
        assertThatThrownBy(() -> Order.create(user, productQuantities, coupon))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("상품 재고 부족");

        verify(product, times(1)).isStockAvailable(quantity);
        verifyNoInteractions(coupon);
    }

    @Test
    @DisplayName("Order 생성 테스트 - 쿠폰 미적용")
    void createOrder_쿠폰미적용() {
        // given
        MockitoAnnotations.openMocks(this);
        long userId = 1L;
        int quantity = 1;
        long productPrice = 10000L;
        User user = User.builder().id(userId).name("Test User").build();

        when(product.getId()).thenReturn(1L);
        when(product.getPrice()).thenReturn(productPrice);
        doNothing().when(product).isStockAvailable(quantity);

        when(coupon.isValid()).thenReturn(false);

        List<Order.ProductQuantity> productQuantities = List.of(new Order.ProductQuantity(product, quantity));

        // when
        Order order = Order.create(user, productQuantities, coupon);

        // then
        assertThat(order.getTotalPrice()).isEqualTo(productPrice * quantity);
        verify(product, times(1)).isStockAvailable(quantity);
        verify(coupon, times(1)).isValid();
        verify(coupon, never()).calculateDiscount(anyLong());
    }
}