package kr.hhplus.be.server.application.product;

import kr.hhplus.be.server.application.product.repository.ProductRepository;
import kr.hhplus.be.server.application.product.service.ProductService;
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

class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private ProductService productService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getById_호출시_상품을_반환한다() {
        // given
        long productId = 1L;
        Product product = mock(Product.class);
        when(productRepository.findById(productId)).thenReturn(product);

        // when
        Product result = productService.getById(productId);

        // then
        assertThat(result).isEqualTo(product);
        verify(productRepository, times(1)).findById(productId);
    }

    @Test
    void getAll_호출시_모든_상품을_반환한다() {
        // given
        List<Product> products = List.of(mock(Product.class), mock(Product.class));
        when(productRepository.findAll()).thenReturn(products);

        // when
        List<Product> result = productService.getAll();

        // then
        assertThat(result).isEqualTo(products);
        verify(productRepository, times(1)).findAll();
    }

    @Test
    void validateStockAvailability_호출시_재고를_검증한다() {
        // given
        Product product = mock(Product.class);
        Order.ProductQuantity productQuantity = new Order.ProductQuantity(product, 10);
        List<Order.ProductQuantity> productQuantities = List.of(productQuantity);

        // when
        productService.validateStockAvailability(productQuantities);

        // then
        verify(product, times(1)).isStockAvailable(10);
    }

    @Test
    void deductStock_호출시_재고를_차감한다() {
        // given
        Product product = mock(Product.class);
        Order.ProductQuantity productQuantity = new Order.ProductQuantity(product, 10);
        List<Order.ProductQuantity> productQuantities = List.of(productQuantity);

        // when
        productService.deductStock(productQuantities);

        // then
        verify(product, times(1)).deduct(10);
        verify(productRepository, times(1)).save(product);
    }
}