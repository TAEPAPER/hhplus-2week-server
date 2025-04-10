package kr.hhplus.be.server.application.order.service;

import kr.hhplus.be.server.application.coupon.repository.CouponRepository;
import kr.hhplus.be.server.application.order.repository.OrderRepository;
import kr.hhplus.be.server.application.product.repository.ProductRepository;
import kr.hhplus.be.server.domain.coupon.Coupon;
import kr.hhplus.be.server.domain.order.Order;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service

public class OrderService {

    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;
    private final CouponRepository couponRepository

    public OrderService(ProductRepository productRepository, OrderRepository orderRepository, CouponRepository couponRepository){
        this.productRepository = productRepository;
        this.orderRepository = orderRepository;
        this.couponRepository = couponRepository;
    }

    public Order placeOrder(long userId, List<Order.ProductQuantity> quantities,  Coupon coupon){

       /* // 물품별 현재 재고를 조회
        List<Order.ProductQuantity> quantities = productQuantities.entrySet().stream()
                .map(entry -> new Order.ProductQuantity(
                        productRepository.findById(entry.getKey()),
                        entry.getValue()
                )).toList();

        // 쿠폰을 조회
        Coupon coupon = couponRepository.findById(couponId);

        Order order = Order.create(userId, quantities, coupon);
        orderRepository.save(order);*/
        Order order = Order.create(userId, quantities, coupon);
        return order;
    }
}

