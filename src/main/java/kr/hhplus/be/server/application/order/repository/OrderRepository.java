package kr.hhplus.be.server.application.order.repository;

import kr.hhplus.be.server.domain.order.Order;

import java.util.Optional;

public interface OrderRepository {


    Order save(Order order);

    Optional<Order> findById(long orderId);
}
