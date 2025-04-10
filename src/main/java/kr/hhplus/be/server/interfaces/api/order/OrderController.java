package kr.hhplus.be.server.interfaces.api.order;

import kr.hhplus.be.server.application.order.service.OrderService;
import kr.hhplus.be.server.domain.order.Order;
import kr.hhplus.be.server.interfaces.api.order.dto.OrderItemRequest;
import kr.hhplus.be.server.interfaces.api.order.dto.OrderRequest;
import kr.hhplus.be.server.interfaces.api.order.dto.OrderResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
public class OrderController implements OrderApi{

    private final OrderService orderService;

    @Override
    @PostMapping("/orders")
    public ResponseEntity<OrderResponse> order(OrderRequest request) {
        Map<Long, Integer> productQuantities = request.getOrderItems().stream()
                .collect(Collectors.toMap(OrderItemRequest::getProductId, OrderItemRequest::getQuantity));

        Order order = orderService.placeOrder(request.getUserId(), productQuantities, request.getCouponId());

        return ResponseEntity.ok(new OrderResponse(order));
    }
}
