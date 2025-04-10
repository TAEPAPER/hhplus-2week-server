package kr.hhplus.be.server.application.order.service;

import kr.hhplus.be.server.application.order.repository.OrderRepository;
import kr.hhplus.be.server.application.product.repository.ProductRepository;
import kr.hhplus.be.server.domain.order.Order;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service

public class OrderService {

    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;

    public OrderService(ProductRepository productRepository, OrderRepository orderRepository) {
        this.productRepository = productRepository;
        this.orderRepository = orderRepository;
    }

    public Order placeOrder(long userId, Map<Long, Integer> productQuantities) {

        // 물품별 재고를 조회
        List<Order.ProductQuantity> quantities = productQuantities.entrySet().stream()
                .map(entry -> new Order.ProductQuantity(
                        productRepository.findById(entry.getKey()),
                        entry.getValue()
                )).toList();

        Order order = Order.create(userId, quantities);
        orderRepository.save(order);
        return order;
    }
}

