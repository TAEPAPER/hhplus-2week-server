package kr.hhplus.be.server.application.product.service;

import kr.hhplus.be.server.application.order.OrderCriteria;
import kr.hhplus.be.server.application.product.repository.InventoryRepository;
import kr.hhplus.be.server.application.product.repository.ProductRepository;
import kr.hhplus.be.server.application.product.repository.TopSellingProductRepository;
import kr.hhplus.be.server.domain.order.Order;
import kr.hhplus.be.server.domain.product.Inventory;
import kr.hhplus.be.server.domain.product.Product;
import kr.hhplus.be.server.domain.product.TopSellingProduct;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import org.springframework.cache.annotation.Cacheable;

@Service
@RequiredArgsConstructor
@EnableScheduling
public class ProductService {

    private final ProductRepository productRepository;
    private final InventoryRepository inventoryRepository;
    private final TopSellingProductRepository topSellingProductRepository;

    public Product getById(long id) {
        return productRepository.findById(id);
    }

    public List<Product> getAll() {
        return productRepository.findAll();
    }

    public List<Order.ProductQuantity> getProductsStock(List<OrderCriteria.OrderProduct> orderProducts) {
        return orderProducts.stream().map(orderProduct -> {
            long productId = orderProduct.getProductId();

            //상품 정보(재고 포함)
            //Product product = productRepository.findById(productId);
            Product product = productRepository.findByIdWithLock(productId)
                    .orElseThrow(() -> new IllegalArgumentException("상품 정보가 없습니다!"));
            //주문 수량
            int orderQuantity = orderProduct.getQuantity();

            return new Order.ProductQuantity(product, orderQuantity);
        }).toList();
    }

    // 재고 확인 로직 - 메서드명 변경
    public void validateStockAvailability(List<Order.ProductQuantity> productQuantities) {
        for (Order.ProductQuantity pq : productQuantities) {
            Product product = pq.product();
            int quantity = pq.quantity();
            product.checkStockAvailability(quantity);
        }
    }

    @Transactional
    public void deductStock(List<Order.ProductQuantity> productQuantities) {
        for (Order.ProductQuantity pq : productQuantities) {
            // 상품 정보(재고 포함)
            Product product = pq.product();
            //현재 재고 정보
            Inventory inventory = pq.product().getInventory();

            // 주문 수량
            int quantity = pq.quantity();

            product.deductStock(quantity);
            // 재고 저장
            inventoryRepository.save(product.getInventory());
        }
    }

    @Cacheable(value = "popularProducts", key = "'popular'")
    public List<TopSellingProduct> getPopularProducts() {
        return topSellingProductRepository.findAll();
    }

    @Scheduled(cron = "0 0 0 * * ?")
    @CacheEvict(value = "popularProducts", key = "'popular'")
    public void refreshPopularProductsCache() {
        // 하루에 한 번 캐시 초기화
        System.out.println("[캐시 초기화] 인기 상품 캐시가 초기화되었습니다.");
    }

    public Product save(Product product) {
        return productRepository.save(product);
    }
}
