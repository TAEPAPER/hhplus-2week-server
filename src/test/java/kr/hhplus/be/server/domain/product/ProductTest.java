package kr.hhplus.be.server.domain.product;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

class ProductTest {

    @Mock
    private Inventory inventory;

    private Product product;

   @BeforeEach
    void setUp() throws Exception {
        MockitoAnnotations.openMocks(this);
        product = new Product();

        // 리플렉션을 사용하여 private 필드에 접근
        java.lang.reflect.Field inventoryField = Product.class.getDeclaredField("inventory");
        inventoryField.setAccessible(true);
        inventoryField.set(product, inventory); // Mock Inventory 주입
    }

    @Test
    @DisplayName("재고가 충분하지 않을 때 checkStockAvailability 메서드가 예외를 던진다")
    void checkStockAvailability_재고부족_예외() {
        // given
        int requestedQuantity = 10;
        when(inventory.hasEnough(requestedQuantity)).thenReturn(false);

        // when & then
        assertThatThrownBy(() -> product.checkStockAvailability(requestedQuantity))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("상품 재고 부족");

        verify(inventory, times(1)).hasEnough(requestedQuantity);
    }

    @Test
    @DisplayName("재고가 충분하지 않을 때 deductStock 메서드가 예외를 던진다")
    void deductStock_재고부족_예외() {
        // given
        int requestedQuantity = 5;
        when(inventory.hasEnough(requestedQuantity)).thenReturn(false);

        // when & then
        assertThatThrownBy(() -> product.deductStock(requestedQuantity))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage("재고 부족으로 차감 불가");

        verify(inventory, times(1)).hasEnough(requestedQuantity);
    }

    @Test
    @DisplayName("재고가 충분할 때 deductStock 메서드가 정상적으로 호출된다")
    void deductStock_정상작동() {
        // given
        int requestedQuantity = 3;
        when(inventory.hasEnough(requestedQuantity)).thenReturn(true);

        // when
        product.deductStock(requestedQuantity);

        // then
        verify(inventory, times(1)).hasEnough(requestedQuantity);
        verify(inventory, times(1)).deduct(requestedQuantity);
    }
}
