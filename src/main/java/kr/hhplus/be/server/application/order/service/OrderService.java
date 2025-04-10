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

    public Order placeOrder(long userId, List<Order.ProductQuantity> quantities,  Coupon coupon){
        Order order = Order.create(userId, quantities, coupon);
        return order;
    }
}

