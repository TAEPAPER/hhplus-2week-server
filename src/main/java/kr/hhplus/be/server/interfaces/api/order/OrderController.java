package kr.hhplus.be.server.interfaces.api.order;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.hhplus.be.server.interfaces.api.order.dto.OrderRequest;
import kr.hhplus.be.server.interfaces.api.order.dto.OrderResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.math.BigDecimal;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@RestController
@RequestMapping("/orders")
@Tag(name = "주문", description = "주문 및 결제 API")
public class OrderController {

    private final Map<Long, BigDecimal> wallet = new ConcurrentHashMap<>();
    private final AtomicLong orderIdGen = new AtomicLong(1);
    private final AtomicLong paymentIdGen = new AtomicLong(1000);

    @Operation(summary = "주문 생성 및 결제 처리")
    @PostMapping
    public ResponseEntity<OrderResponse> order(@RequestBody OrderRequest request) {
        BigDecimal total = request.getItems().stream()
            .map(item -> new BigDecimal("10000").multiply(BigDecimal.valueOf(item.getQuantity())))
            .reduce(BigDecimal.ZERO, BigDecimal::add);

        //BigDecimal balance = wallet.getOrDefault(request.getUserId(), BigDecimal.ZERO);
        BigDecimal balance = new BigDecimal("50000");
        if (balance.compareTo(total) < 0) {
            return ResponseEntity.badRequest().build(); // 잔액 부족
        }

        wallet.put(request.getUserId(), balance.subtract(total));
        return ResponseEntity.ok(new OrderResponse(
            orderIdGen.getAndIncrement(),
            paymentIdGen.getAndIncrement(),
            total
        ));
    }
}
