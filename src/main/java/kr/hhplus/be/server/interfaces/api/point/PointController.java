package kr.hhplus.be.server.interfaces.api.point;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import kr.hhplus.be.server.interfaces.api.point.dto.PointChargeRequest;
import kr.hhplus.be.server.interfaces.api.point.dto.PointResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@RestController
@RequestMapping("/point")
@Tag(name = "잔액", description = "잔액 충전 및 조회 API")
public class PointController {

    private final Map<Long, BigDecimal> wallet = new ConcurrentHashMap<>();

    @Operation(summary = "잔액 충전")
    @PostMapping("/charge")
    public ResponseEntity<Void> charge(@RequestBody PointChargeRequest request) {
        wallet.merge(request.getUserId(), request.getAmount(), BigDecimal::add);
        return ResponseEntity.ok().build();
    }

    @Operation(summary = "잔액 조회")
    @GetMapping("/{userId}")
    public ResponseEntity<PointResponse> get(@PathVariable("userId") Long userId) {
        BigDecimal point = wallet.getOrDefault(userId, BigDecimal.ZERO);
        return ResponseEntity.ok(new PointResponse(userId, point));
    }
}
