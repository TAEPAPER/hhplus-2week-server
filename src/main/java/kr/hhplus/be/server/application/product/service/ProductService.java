package kr.hhplus.be.server.application.product.service;

import kr.hhplus.be.server.application.product.repository.ProductRepository;
import kr.hhplus.be.server.domain.order.Order;
import kr.hhplus.be.server.domain.product.Product;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    public Product getById(long id) {
        return productRepository.findById(id);
    }

    public List<Product> getAll() {
        return productRepository.findAll();
    }


    public List<Order.ProductQuantity> getProductsStock(Map<Long, Integer> productQuantities) {
        return productQuantities.entrySet().stream()
                .map(entry -> new Order.ProductQuantity(
                        productRepository.findById(entry.getKey()),
                        entry.getValue()
                )).toList();
    }

    // ProductService에 재고 확인 로직 추가
    public void validateStockAvailability(List<Order.ProductQuantity> productQuantities) {
        for (Order.ProductQuantity pq : productQuantities) {
            Product product = pq.product();
            int quantity = pq.quantity();
            product.isStockAvailable(quantity);
        }
    }
}
