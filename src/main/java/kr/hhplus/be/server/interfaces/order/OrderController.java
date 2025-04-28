package kr.hhplus.be.server.interfaces.order;

import kr.hhplus.be.server.application.order.OrderFacade;
import kr.hhplus.be.server.domain.order.Order;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/orders")
public class OrderController implements OrderApi{

    private final OrderFacade orderFacade;

    @Override
    @PostMapping("/order")
    public ResponseEntity<OrderResponse> order(OrderRequest request) {
        Order order = orderFacade.order(request.toCriteria());
        return ResponseEntity.ok(new OrderResponse(order));
    }
}
