package kr.hhplus.be.server.application.product;

import kr.hhplus.be.server.application.product.repository.InventoryRepository;
import kr.hhplus.be.server.application.product.repository.ProductRepository;
import kr.hhplus.be.server.application.product.repository.TopSellingProductRepository;
import kr.hhplus.be.server.application.product.service.ProductService;
import kr.hhplus.be.server.domain.order.Order;
import kr.hhplus.be.server.domain.product.Inventory;
import kr.hhplus.be.server.domain.product.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private InventoryRepository inventoryRepository;

    @Mock
    private TopSellingProductRepository topSellingProductRepository;

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
    void createInventory_호출시_재고를_생성한다() {
        // given
       /* long productId = 1L;
        int initialStock = 100;
        Product product = mock(Product.class);
        Inventory inventory = mock(Inventory.class);

        when(productRepository.findById(productId)).thenReturn(product);
        when(inventoryRepository.createForProduct(product, initialStock)).thenReturn(inventory);

        // when
        Inventory result = productService.createInventory(productId, initialStock);

        // then
        assertThat(result).isEqualTo(inventory);
        verify(productRepository, times(1)).findById(productId);
        verify(inventoryRepository, times(1)).createForProduct(product, initialStock);*/
    }

    @Test
    void updateInventory_재고가_존재할때_재고를_업데이트한다() {
   /*     // given
        long productId = 1L;
        int newStock = 50;
        int currentStock = 30;
        int difference = newStock - currentStock;

        Product product = mock(Product.class);
        Inventory inventory = mock(Inventory.class);

        when(inventoryRepository.findByProductId(productId)).thenReturn(Optional.of(inventory));
        when(inventory.getStock()).thenReturn(currentStock);
        when(inventoryRepository.save(inventory)).thenReturn(inventory);

        // when
        Inventory result = productService.updateInventory(productId, newStock);

        // then
        assertThat(result).isEqualTo(inventory);
        verify(inventory, times(1)).addStock(difference);
        verify(inventoryRepository, times(1)).save(inventory);*/
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
        verify(product, times(1)).checkStockAvailability(10);
    }

    @Test
    void deductStock_호출시_재고를_차감한다() {
        // given
       /* long productId = 1L;
        Product product = mock(Product.class);
        Inventory inventory = mock(Inventory.class);
        Order.ProductQuantity productQuantity = new Order.ProductQuantity(product, 10);
        List<Order.ProductQuantity> productQuantities = List.of(productQuantity);

        when(product.getId()).thenReturn(productId);
        when(inventoryRepository.findByProductId(productId)).thenReturn(Optional.of(inventory));

        // when
        productService.deductStock(productQuantities);

        // then
        verify(product, times(1)).deductStock(10);
        verify(inventoryRepository, times(1)).save(inventory);*/
    }
}
